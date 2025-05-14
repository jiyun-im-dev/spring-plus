package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryQuery {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("""
            SELECT t
            FROM Todo t
            WHERE (:weather IS NULL OR t.weather LIKE CONCAT('%', :weather, '%'))
              AND (:start IS NULL OR t.modifiedAt >= :start)
              AND (:end IS NULL OR t.modifiedAt < :end)
            """)
    Page<Todo> findByConditions(Pageable pageable, String weather, LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);
}
