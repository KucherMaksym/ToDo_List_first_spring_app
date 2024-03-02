package com.example.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.testspring.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Метод для поиска задачи по ее идентификатору
    Optional<Task> findById(long id);

    List<Task> findAll();

    // Метод для сохранения задачи
    Task save(Task task);

    // Метод для удаления задачи
    void delete(Task task);
}
