package servlet;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import entity.Book;
import service.BookService;
import utils.RestRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "ВоокServlet", urlPatterns = "/book/*")
public class BookServlet extends HttpServlet {
    public BookServlet() {
        super();
    }
    @Override
    public void init() throws ServletException {
    }

    private static final BookService bookService = BookService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getParameter("id");
        PrintWriter out = resp.getWriter();
        System.out.println(pathInfo);

        out.flush();

        try {
            if(pathInfo==null) throw new ServletException();
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
            out.println(e);
        }
        catch (NumberFormatException e) {
            resp.setStatus(404);
            return;
        }

        String json;
        if (pathInfo != null && pathInfo.equals("/" + Objects.requireNonNull(pathInfo))){
            Book bookById = bookService.getByID(2L);
            json = jsonMapper.writeValueAsString(bookById);
        } else {
            List<Book> books = bookService.getAll();
            json = jsonMapper.writeValueAsString(books);
        }

        if (!json.equals("null")) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
        } else {
            resp.setStatus(404);
        }

        out.write(json);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        RestRequest restRequest = null;
        try {
            restRequest = new RestRequest(req.getPathInfo());
        } catch (NumberFormatException e) {
            resp.setStatus(404);
            return;
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if ((restRequest != null ? restRequest.getId() : null) == null) {
            try (BufferedReader reader = req.getReader()) {
                Book book = jsonMapper.readValue(reader, Book.class);
                Book savedBook = bookService.save(book);
                resp.sendRedirect("/book/" + savedBook.getId());
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
        } catch (NumberFormatException e) {
            resp.setStatus(404);
            return;
        } catch (ServletException e) {
            resp.setStatus(400);
            resp.resetBuffer();
            e.printStackTrace();
        }

        if ((restRequest != null ? restRequest.getId() : null) != null) {
            try (BufferedReader reader = req.getReader()) {
                Book book = jsonMapper.readValue(reader, Book.class);
                bookService.update(book);
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
            boolean delete = bookService.delete(restRequest.getId());
            resp.getWriter().write(String.valueOf(delete));
        } else {
            doGet(req, resp);
        }
    }
}


