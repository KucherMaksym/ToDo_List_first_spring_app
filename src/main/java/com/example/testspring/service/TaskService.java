package com.example.testspring.service;


import com.example.testspring.model.Task;
import com.example.testspring.repository.TaskRepository;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    @Transactional
    public List<Task> getAllTask() {
        return  taskRepository.findAll();
    }

    @Transactional
    public Optional<Task> getTaskById(long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public void deleteById(long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }



}
