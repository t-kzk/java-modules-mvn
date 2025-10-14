package org.kzk.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kzk.entity.Event;
import org.kzk.entity.FileE;
import org.kzk.entity.Writer;
import org.kzk.repository.EventsRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventsRepository eventsRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void createEventTest() {
        Writer writer = new Writer(
                1, "userNamame", new ArrayList<>()
        );
        FileE file = new FileE(
                1, "fileName", "Path"
        );
        Event event = new Event(1, writer, file);
        Mockito.when(eventsRepository.save(Mockito.any()))
                .thenReturn(event);

        Event result = eventService.createEvent(writer, file);

        assertEquals(event.getId(), result.getId());
    }

}