package org.kzk.service;

import org.kzk.entity.Event;
import org.kzk.entity.FileE;
import org.kzk.entity.Writer;
import org.kzk.repository.EventsRepository;
import org.kzk.repository.impl.EventRepositoryImpl;

public class EventService {

    EventsRepository eventsRepository = new EventRepositoryImpl();

    public Event createEvent(Writer writer, FileE fileE) {
        return eventsRepository.save(Event.builder()
                .file(fileE)
                .writer(writer)
                .build());
    }

}
