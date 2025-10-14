package org.kzk.service;

import org.kzk.entity.Event;
import org.kzk.entity.FileE;
import org.kzk.entity.Writer;
import org.kzk.repository.EventsRepository;
import org.kzk.repository.impl.EventRepositoryImpl;

public class EventService {

    private final EventsRepository eventsRepository;

    public EventService() {
        eventsRepository = new EventRepositoryImpl();
    }

    public EventService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    public Event createEvent(Writer writer, FileE fileE) {
        return eventsRepository.save(Event.builder()
                .file(fileE)
                .writer(writer)
                .build());
    }

}
