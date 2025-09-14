package org.kzk.demo;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html");
        resp.getWriter().write(
                "<!DOCTYPE html>" +
                        "<html><head><title>File Upload</title></head>" +
                        "<body>" +
                        "<h2>Upload File</h2>" +
                        "<form action='/upload' method='post' enctype='multipart/form-data'>" +
                        "   <input type='file' name='file' required><br><br>" +
                        "   <input type='submit' value='Upload'>" +
                        "</form>" +
                        "</body></html>"
        );
    }
}
