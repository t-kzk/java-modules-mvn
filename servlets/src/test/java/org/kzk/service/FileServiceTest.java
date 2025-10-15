package org.kzk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.kzk.entity.Event;
import org.kzk.entity.FileE;
import org.kzk.entity.Writer;
import org.kzk.repository.FilesRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FilesRepository filesRepository;

    @Mock
    private EventService eventService;

    @TempDir
    Path tempDir;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileService(
                filesRepository, tempDir, eventService);
    }

    @Test
    void createFileTest() throws Exception {
        final String fileName = "fileName";
        final Writer writer = new Writer(
                1, "userNamame", new ArrayList<>()
        );
        final InputStream inputStream =
                new ByteArrayInputStream("file".getBytes(StandardCharsets.UTF_8));

        FileE.FileEBuilder builder = FileE.builder()
                .name(fileName)
                .filePath(tempDir.resolve(fileName).toString());
        Mockito.when(filesRepository.save(any()))
                .thenReturn(builder.id(1).build());
        Mockito.when(eventService.createEvent(Mockito.any(), Mockito.any()))
                .thenReturn(new Event());

        FileE result = fileService.createFile(fileName, writer, inputStream);

        assertEquals(1, result.getId());
    }

}