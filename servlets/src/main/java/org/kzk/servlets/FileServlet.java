package org.kzk.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kzk.entity.FileE;
import org.kzk.entity.Writer;
import org.kzk.service.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "FileServlet", urlPatterns = {"/api/files/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB — в памяти, больше — во временный файл
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50  // 50MB
)
public class FileServlet extends HttpServlet {

    private final FileService fileService = new FileService();
    private final ObjectMapper om = new ObjectMapper();

    /**
     * POST /api/files
     * Загружает новый файл (multipart/form-data, поле "file").
     */
    @Override
    //todo такие исключения пойдут в контейнер Tomcat, который покажет стандартную страницу ошибки 500
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Writer currentUser = getCurrentUser(req, resp);
        if (currentUser == null) return;

        // Получаем часть "file" из формы
        Part filePart = req.getPart("file");
        if (filePart == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Please put 'file'!");
            return;
        }

        String originalName = filePart.getSubmittedFileName();
        try {
            // Сохраняем файл на диск + запись в БД
            FileE saved = fileService.createFile(originalName, currentUser, filePart.getInputStream());

            // Отправляем обратно JSON с метаданными
            resp.setContentType("application/json");
            om.writeValue(resp.getOutputStream(), saved);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("Error:  the file is exist " + e.getMessage());
        }
    }


    /**
     * GET /api/files
     * Возвращает список всех файлов в JSON.
     * <p>
     * GET /api/files/{id}
     * Возвращает сам файл для скачивания.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // "/{fileId}" или null
        if (pathInfo == null || pathInfo.equals("/")) {
            // Вернуть список файлов
            List<FileE> files = fileService.findAll();
            resp.setContentType("application/json");
            om.writeValue(resp.getOutputStream(), files);
            return;
        } else {
            String fileId = pathInfo.replace("/", "");

            Optional<FileE> byId = fileService.findById(Integer.valueOf(fileId));

            if (byId.isPresent()) {
                FileE file = byId.get();
                Path filePath = fileService.getFilePath(file.getFilePath());

                if (Files.exists(filePath)) {
                    resp.setStatus(200);
                    resp.getWriter().write("http://localhost:9595//api/download/" + file.getFilePath());
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Error:  file not found!");

                }
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Error:  file not found!");
            }
        }

    }

    /**
     * DELETE /api/files/{id}
     * Удаляет файл + инф из БД
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "File id not exist");
        } else {
            String fileId = pathInfo.replace("/", "");

            try {
                int id = Integer.parseInt(fileId);
                fileService.deleteFile(id);

            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid file id: " + fileId);
            }
        }
    }

    /**
     * Получает текущего залогиненного пользователя из сессии.
     * Если сессии нет или пользователь не найден, возвращает null.
     */
    private Writer getCurrentUser(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        Writer writer = (Writer) session.getAttribute("user");
        return writer;
    }
}
