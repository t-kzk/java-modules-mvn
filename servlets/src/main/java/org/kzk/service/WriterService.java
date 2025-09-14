package org.kzk.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.kzk.entity.Writer;
import org.kzk.repository.WritersRepository;
import org.kzk.repository.impl.WriterRepositoryImpl;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class WriterService extends HttpServlet {
    private WritersRepository writersRepository;


    @Override
    public void init() {
        writersRepository = new WriterRepositoryImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        Optional<Writer> byName = writersRepository.findByName(name);
        Writer writer = byName.orElseGet(() -> {
            Writer build = Writer.builder()
                    .username(name)
                    .build();
            return writersRepository.save(build);
        });

        HttpSession session = req.getSession(true);
        session.setAttribute("user", writer);

        resp.getWriter().write("Залогинено как: " + writer.getUsername());
    }
}
