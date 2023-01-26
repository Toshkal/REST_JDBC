package servlet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.Author;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AuthorService;
import utils.RestRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "AuthorServlet", urlPatterns = "/autor/*")
public class AuthorServlet extends HttpServlet {
    public AuthorServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
    private static final AuthorService authorService = AuthorService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RestRequest restRequest = null;
        PrintWriter out = resp.getWriter();
        Enumeration<String> parameterNames = req.getParameterNames();
        String pathInfo = null;
        while (parameterNames.hasMoreElements()){
            String s = parameterNames.nextElement();
            if(s.equals("id")) pathInfo = s;
        }
        String parameter = req.getParameter("id");

        String json;
        if (pathInfo != null ) {
            Author authorId = authorService.getByID(Long.parseLong(parameter));
            json = jsonMapper.writeValueAsString(authorId);
        } else {
            List<Author> authors = authorService.getAll();
            json = jsonMapper.writeValueAsString(authors);
        }

        if (!json.equals("null")    ) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.write(json + "\n");
        } else {
            resp.setStatus(404);
        }

        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RestRequest restRequest = null;
        try {
            restRequest = new RestRequest(req.getPathInfo());
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if (Objects.requireNonNull(restRequest).getId() == null) {
            try (BufferedReader reader = req.getReader()) {
                Author author = jsonMapper.readValue(reader, Author.class);
                Author savedAuthor = authorService.save(author);
                resp.sendRedirect("/autor/" + savedAuthor.getId());
            }
        } else {
            resp.setStatus(201);
            doGet(req, resp);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RestRequest restRequest = null;
        try {
            restRequest = new RestRequest(req.getPathInfo());
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if (Objects.requireNonNull(restRequest).getId() != null) {
            try(BufferedReader reader = req.getReader()) {
                Author author = jsonMapper.readValue(reader, Author.class);
                authorService.update(author);
                doGet(req, resp);
            }
        } else {
            resp.setStatus(404);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RestRequest restRequest = null;
        try {
            restRequest = new RestRequest(req.getPathInfo());
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if (Objects.requireNonNull(restRequest).getId() != null) {
            boolean delete = authorService.delete(restRequest.getId());
            resp.getWriter().write(String.valueOf(delete));
        } else {
            doGet(req, resp);
        }
    }
}
