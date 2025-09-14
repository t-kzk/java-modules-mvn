package org.kzk.repository.impl;

import org.kzk.entity.Writer;
import org.kzk.jpa.EmfProvider;
import org.kzk.jpa.JpaService;
import org.kzk.repository.WritersRepository;

import java.util.List;
import java.util.Optional;

public class WriterRepositoryImpl extends JpaService implements WritersRepository {
    public WriterRepositoryImpl() {
        super(EmfProvider.INSTANCE.getEmf().createEntityManager());
    }

    @Override
    public Writer save(Writer entity) {
        return merge(entity);
    }

    @Override
    public Writer update(Writer entity) {
        return merge(entity);
    }

    @Override
    public boolean delete(Writer entity) {
        em.remove(entity);
        return true;
    }

    @Override
    public Optional<Writer> findById(Integer id) {
        Writer singleResult = em.createQuery("""
                select w from Writer w left join fetch w.events e
                join fetch e.file where w.id = :id
                """, Writer.class).setParameter("id", id).getSingleResult();
        return Optional.ofNullable(singleResult);
    }

    @Override
    public List<Writer> findAll() {
        return em.createQuery("""
                select w from Writer w left join fetch w.events e
                join fetch e.file
                """, Writer.class).getResultList();
    }


    @Override
    public Optional<Writer> findByName(String name) {
        Writer singleResult = em.createQuery("""
                select w from Writer w left join fetch w.events e
                left join fetch e.file where w.username = :name
                """, Writer.class).setParameter("name", name).getSingleResultOrNull();
        return Optional.ofNullable(singleResult);
    }
}
