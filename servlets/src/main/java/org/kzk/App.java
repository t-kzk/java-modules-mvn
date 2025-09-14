package org.kzk;

import jakarta.servlet.MultipartConfigElement;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.kzk.demo.FileUploadServlet;
import org.kzk.demo.TestServlet;
import org.kzk.service.WriterService;
import org.kzk.servlets.FileServlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws LifecycleException, IOException {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(9595);
        tomcat.getConnector();

        // ВАЖНО: создаем временную директорию для Tomcat
        File tempDir = Files.createTempDirectory("tomcat-").toFile();
        tempDir.deleteOnExit();

        // 1. Создаем временный контекст БЕЗ webapp папки
        Context ctx = tomcat.addContext("", tempDir.getAbsolutePath());

        // 2. РЕГИСТРИРУЕМ СЕРВЛЕТ ЧЕРЕЗ КОД (без web.xml!)
        Tomcat.addServlet(ctx, "fileUploadServlet", new FileUploadServlet());
        ctx.addServletMappingDecoded("/upload", "fileUploadServlet");

        // 3. Добавляем тестовый сервлет
        Tomcat.addServlet(ctx, "loginWriter", new WriterService());
        ctx.addServletMappingDecoded("/login", "loginWriter");
        ctx.addServletMappingDecoded("", "loginWriter"); // корневой путь

        Wrapper wrapper = Tomcat.addServlet(ctx, "fileServlet", new FileServlet());
        wrapper.setMultipartConfigElement(
                new MultipartConfigElement(System.getProperty("java.io.tmpdir"),
                        10 * 1024 * 1024,
                        50 * 1024 * 1024,
                        1 * 1024 * 1024)
        );
        ctx.addServletMappingDecoded("/api/files/*", "fileServlet");



        // 4. Запускаем
        tomcat.start();
        System.out.println("✅ Tomcat started: http://localhost:9595");
        System.out.println("📁 Upload: http://localhost:9595/upload");
        System.out.println("🔧 Test: http://localhost:9595/test");

        tomcat.getServer().await();

    }
}
