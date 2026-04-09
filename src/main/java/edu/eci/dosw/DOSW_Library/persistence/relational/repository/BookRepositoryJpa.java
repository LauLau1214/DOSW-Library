package edu.eci.dosw.DOSW_Library.persistence.relational.repository;

import edu.eci.dosw.DOSW_Library.persistence.relational.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepositoryJpa extends JpaRepository<BookEntity, String> {
}