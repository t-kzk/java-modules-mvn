package org.kzk.repository.impl;

import org.kzk.entity.FileE;
import org.kzk.jpa.EmfProvider;
import org.kzk.jpa.JpaService;
import org.kzk.repository.FilesRepository;

import java.util.List;
import java.util.Optional;

public class FileRepositoryImpl extends JpaService implements FilesRepository {
    public FileRepositoryImpl() {
        super(EmfProvider.INSTANCE.getEmf().createEntityManager());
    }

    @Override
    public FileE save(FileE entity) {
        return merge(entity);
    }

    @Override
    public FileE update(FileE entity) {
        return merge(entity);
    }

    @Override
    public boolean delete(FileE entity) {
        em.remove(entity);
        return true;
    }

    @Override
    public Optional<FileE> findById(Integer integer) {
        FileE file = em.find(FileE.class, integer);
        return Optional.ofNullable(file);
    }

    @Override
    public List<FileE> findAll() {
        return em.createQuery("""
                select f from FileE f
                """, FileE.class).getResultList();
    }
}
