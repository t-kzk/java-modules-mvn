package org.kzk.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.kzk.entity.Event;
import org.kzk.entity.FileE;
import org.kzk.entity.Writer;
import org.kzk.service.EventService;
import org.kzk.service.FileService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FileServlet", urlPatterns = {"/api/files/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB — в памяти, больше — во временный файл
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50  // 50MB
)
public class FileServlet extends HttpServlet {

    private FileService fileService;
    private EventService eventService;
    private ObjectMapper om;

    @Override
    public void init() throws ServletException {
        fileService = new FileService();
        eventService = new EventService();
        om = new ObjectMapper();
    }

    /**
     * POST /api/files
     * Загружает новый файл (multipart/form-data, поле "file").
     */
    @Override
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
            FileE saved = fileService.createFile(originalName, filePart.getInputStream());
            Event event = eventService.createEvent(currentUser, saved);
            // Отправляем обратно JSON с метаданными
            resp.setContentType("application/json");
            om.writeValue(resp.getOutputStream(), event);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error: " + e.getMessage());
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // пример: "/5" или null
        if (pathInfo == null || pathInfo.equals("/")) {
            // Вернуть список файлов
            List<FileE> files = new ArrayList<>();
            resp.setContentType("application/json");
            om.writeValue(resp.getOutputStream(), files);
            return;
        }

    }

    /**
     * Получает текущего залогиненного пользователя из сессии.
     * Если сессии нет или пользователь не найден, возвращает null.
     */
    private Writer getCurrentUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false); // не создаём новую сессию
        if (session == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Нет сессии, пожалуйста залогиньтесь");
            return null;
        }

        Writer writer = (Writer) session.getAttribute("user");
        if (writer == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("Сессия есть, но пользователь не найден");
            return null;
        }

        return writer;
    }
}
