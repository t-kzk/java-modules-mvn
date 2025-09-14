package org.kzk.repository;

import org.kzk.entity.Writer;

import java.util.Optional;

public interface WritersRepository extends GenericRepository<Writer, Integer> {

    Optional<Writer> findByName(String name);
}
