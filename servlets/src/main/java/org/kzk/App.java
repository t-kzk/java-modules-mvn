package org.kzk;

import jakarta.servlet.MultipartConfigElement;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.BasicConfigurator;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.kzk.filter.AuthFilter;
import org.kzk.servlets.DownloadServlet;
import org.kzk.servlets.FileServlet;
import org.kzk.servlets.WriterServlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws LifecycleException, IOException {
        BasicConfigurator.configure();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(9595);
        tomcat.getConnector();

        // Временная директория для томкат
        File tempDir = Files.createTempDirectory("tomcat-").toFile();
        tempDir.deleteOnExit();

        // Создание временного контекста БЕЗ webapp папки (папку можно и добавить если тянуть из нее вьюшки)
        Context ctx = tomcat.addContext("", tempDir.getAbsolutePath());

        // регистрация сервлета
        Tomcat.addServlet(ctx, "loginWriter", new WriterServlet());
        ctx.addServletMappingDecoded("/login", "loginWriter");
        ctx.addServletMappingDecoded("", "loginWriter"); // корневой путь

        // регистрация сервлета + ручная настройка загрузки файлов
        Wrapper wrapper = Tomcat.addServlet(ctx, "fileServlet", new FileServlet());
        wrapper.setMultipartConfigElement(
                new MultipartConfigElement(System.getProperty("java.io.tmpdir"),
                        10 * 1024 * 1024,
                        50 * 1024 * 1024,
                        1 * 1024 * 1024)
        );
        ctx.addServletMappingDecoded("/api/files/*", "fileServlet");

        // регистрация сервлета скачивания
        Tomcat.addServlet(ctx, "DownloadServlet", new DownloadServlet());
        ctx.addServletMappingDecoded("/api/download/*", "DownloadServlet");

        // регистрация фильтра
        FilterDef authDef = new FilterDef();
        authDef.setFilterName("authFilter");
        authDef.setFilter(new AuthFilter());
        ctx.addFilterDef(authDef);

        FilterMap authMap = new FilterMap();
        authMap.setFilterName("authFilter");
        authMap.addURLPattern("/api/files/*");
        ctx.addFilterMap(authMap);


        // Запуск
        tomcat.start();
        System.out.println("Tomcat started: http://localhost:9595");
        System.out.println("Upload: http://localhost:9595/api/files");
        System.out.println("Login: http://localhost:9595/login");

        tomcat.getServer().await();

    }
}
