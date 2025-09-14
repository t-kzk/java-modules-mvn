package org.kzk.service;

import org.kzk.entity.FileE;
import org.kzk.repository.FilesRepository;
import org.kzk.repository.impl.FileRepositoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileService {

    private final FilesRepository filesRepository = new FileRepositoryImpl();
    private final Path storageRoot;

    public FileService() {
        this.storageRoot = Path.of(System.getProperty("user.dir"));
    }

    public FileE createFile(String originalName, InputStream content) throws Exception {
        String filename = System.currentTimeMillis() + "_" + originalName;

        Path target = storageRoot.resolve(filename);
        Files.createDirectories(storageRoot);
        Files.copy(content, target, StandardCopyOption.REPLACE_EXISTING);

        FileE fileSave = FileE.builder().name(originalName).filePath(target.toString()).build();

        return filesRepository.save(fileSave);
    }



}
