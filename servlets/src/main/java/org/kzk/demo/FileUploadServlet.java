package org.kzk.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

// АННОТАЦИИ заменяют web.xml!
@WebServlet("/upload") // URL mapping
@MultipartConfig(      // Конфигурация для загрузки файлов
        maxFileSize = 10 * 1024 * 1024,    // 10MB
        maxRequestSize = 50 * 1024 * 1024  // 50MB
)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     int a = 0;
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        try {
            Part filePart = request.getPart("file");
            String fileName = getFileName(filePart);

            if (fileName == null || fileName.isEmpty()) {
                response.sendError(400, "No file selected");
                return;
            }

            // Здесь будет ваша логика обработки файла
            // Пока просто отвечаем успехом
            response.getWriter().write("✅ File received: " + fileName);

        } catch (Exception e) {
            response.sendError(500, "Error: " + e.getMessage());
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition != null) {
            for (String token : contentDisposition.split(";")) {
                if (token.trim().startsWith("filename")) {
                    return token.substring(token.indexOf('=') + 1)
                            .trim()
                            .replace("\"", "");
                }
            }
        }
        return null;
    }
}
