package org.kzk.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kzk.service.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@WebServlet(name = "DownloadServlet", urlPatterns = {"/api/download/*"})
public class DownloadServlet extends HttpServlet {

    private final FileService fileService = new FileService();

    /**
     * GET /api/download/{path}
     * При вызове из браузера должен загрузиться файл (итоговая ссылка является ответом GET /api/files/{id})
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (Objects.nonNull(pathInfo)) {
            Path filePath = fileService.getFilePath(pathInfo);
            if (Files.exists(filePath)) {
                // необходимые хэдеры для браузера (для скачивания)
                resp.setContentType("application/octet-stream"); // атрибут бинарности
                resp.setHeader("Content-Disposition",
                        "attachment; filename=\"" + filePath + "\""); // предложение скачать
                resp.setContentLengthLong(Files.size(filePath)); // инф о размере файла

                // Отправляем файл (файл в стрим resp)
                Files.copy(filePath, resp.getOutputStream());
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Error:  file not found!");

            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error:  file not found!");

        }


    }
}
