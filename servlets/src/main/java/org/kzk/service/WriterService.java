package org.kzk.service;

import org.kzk.entity.Writer;
import org.kzk.repository.WritersRepository;
import org.kzk.repository.impl.WriterRepositoryImpl;

import java.util.Optional;


public class WriterService {
    private final WritersRepository writersRepository;

    public WriterService() {
        writersRepository = new WriterRepositoryImpl();
    }

    public WriterService(WritersRepository writersRepository) {
        this.writersRepository = writersRepository;
    }

    public Writer computeIfAbsentWriter(String name) {
        Optional<Writer> byName = writersRepository.findByName(name);
        Writer writer = byName.orElseGet(() -> {
            Writer build = Writer.builder()
                    .username(name)
                    .build();
            return writersRepository.save(build);
        });

        return writer;
    }

    public Optional<Writer> findByName(String name) {
        Optional<Writer> byName = writersRepository.findByName(name);
        return byName;
    }
}
