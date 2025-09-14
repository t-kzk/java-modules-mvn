package org.kzk.repository.impl;

import jakarta.persistence.EntityManager;
import org.kzk.entity.Event;
import org.kzk.jpa.EmfProvider;
import org.kzk.jpa.JpaService;
import org.kzk.repository.EventsRepository;

import java.util.List;
import java.util.Optional;

public class EventRepositoryImpl extends JpaService implements EventsRepository {
    public EventRepositoryImpl() {
        super(EmfProvider.INSTANCE.getEmf().createEntityManager());
    }

    @Override
    public Event save(Event entity) {
        return merge(entity);
    }

    @Override
    public Event update(Event entity) {
        return null;
    }

    @Override
    public boolean delete(Event entity) {
        return false;
    }

    @Override
    public Optional<Event> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public List<Event> findAll() {
        return List.of();
    }
}
