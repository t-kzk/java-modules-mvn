package org.kzk.service;

import org.kzk.entity.Event;
import org.kzk.entity.FileE;
import org.kzk.entity.Writer;
import org.kzk.repository.FilesRepository;
import org.kzk.repository.impl.FileRepositoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

public class FileService {

    private final FilesRepository filesRepository = new FileRepositoryImpl();
    private final Path storageRoot;
    private final EventService eventService = new EventService();

    public FileService() {
        this.storageRoot = Path.of(System.getProperty("user.dir"));
    }

    public FileE createFile(
            String originalName,
            Writer currentUser,
            InputStream content) throws Exception {

        Path target = storageRoot.resolve(originalName);
        Files.createDirectories(storageRoot);
        Files.copy(content, target, StandardCopyOption.COPY_ATTRIBUTES);

        FileE fileSave = FileE.builder()
                .name(originalName)
                .filePath(target.toString())
                .build();

        FileE saved = filesRepository.save(fileSave);
        Event event = eventService.createEvent(currentUser, saved);
        return saved;
    }

    public Event updateFile(
            String originalName,
            Writer currentUser,
            InputStream content) throws IOException {
        FileE byName = filesRepository.findByName(originalName);
        Path target = storageRoot.resolve(originalName);
        Files.createDirectories(storageRoot);
        Files.copy(content, target, StandardCopyOption.REPLACE_EXISTING);
        Event event = eventService.createEvent(currentUser, byName);
        return event;
    }

    public boolean deleteFile(int fileId) {
        Optional<FileE> byId = filesRepository.findById(fileId);

        if (byId.isPresent()) {
            FileE fileForDel = byId.get();

            return filesRepository.delete(fileForDel);
        } else {
            return false;
        }
    }

    public List<FileE> findAll() {
        return filesRepository.findAll();
    }

    public Optional<FileE> findById(int fileId) {
        return filesRepository.findById(fileId);
    }

    public Path getFilePath(String filePath) {
        return storageRoot.resolve(filePath);
    }

}
