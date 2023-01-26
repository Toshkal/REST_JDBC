package utils;

import jakarta.servlet.ServletException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestRequest {
    private static final Pattern regExIdPattern = Pattern.compile("/([0-9]*)");
    private Long id;

    public RestRequest(String pathInfo) throws ServletException {
        if (pathInfo == null || pathInfo.equals("/")) {
            return;
        }

        Matcher matcher = regExIdPattern.matcher(pathInfo);
        if (matcher.find()) {
            id = Long.parseLong(matcher.group(1));
            return;
        }

        throw new ServletException("Invalid URI");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
