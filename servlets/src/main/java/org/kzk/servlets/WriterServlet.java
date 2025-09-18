package org.kzk.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.kzk.entity.Writer;
import org.kzk.service.WriterService;

import java.io.IOException;

@WebServlet("/login")
public class WriterServlet extends HttpServlet {
    private WriterService writerService;


    @Override
    public void init() {
        writerService = new WriterService();
    }

    /**
     *  Устанавливает куки существующему пользователю (создает сессию).
     *  В случае отсутствия пользователя - создает его + устанавливает куки.
     *  POST /login (multipart/form-data, поле "name").
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //  Получаем часть "name" из формы
        String name = req.getParameter("name");
        Writer writer = writerService.computeIfAbsent(name);

        // Создание сессии - ассоциация с объектом writer
        HttpSession session = req.getSession(true);
        session.setAttribute("user", writer);

        resp.getWriter().write("Залогинено как: " + writer.getUsername());
    }
}
