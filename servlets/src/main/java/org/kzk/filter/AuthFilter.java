package org.kzk.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.kzk.entity.Writer;
import org.kzk.repository.WritersRepository;
import org.kzk.service.WriterService;

import java.io.IOException;
import java.util.Optional;

@WebFilter(urlPatterns = {"/api/files/*"})
public class AuthFilter implements Filter {

    private final WriterService writerService = new WriterService();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false); // не создаём новую сессию
        resp.setContentType("text/html; charset=UTF-8"); // Установка кодировки для ответа

        if (session == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Нет сессии, пожалуйста залогиньтесь");
            return;
        } else {
            Writer writer = (Writer) session.getAttribute("user");
            Optional<Writer> byName = writerService.findByName(writer.getUsername());
            if (byName.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Нет сессии, пожалуйста залогиньтесь");
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
