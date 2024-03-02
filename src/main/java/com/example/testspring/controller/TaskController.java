package com.example.testspring.controller;

import com.example.testspring.model.Task;
import com.example.testspring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<Task>> getAllTask() {
//        List<Task> tasks  = taskService.getAllTask();
//        return new ResponseEntity<>(tasks, HttpStatus.OK);
//    }
    @GetMapping("/all")
    public List<Task> getAllTask() {
        return taskService.getAllTask();
    }

    @PostMapping("/create")
    public Task createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return createdTask;
    }

//    @GetMapping("/{id}")
//    @ResponseBody
//    public Optional<Task> getTaskById(@PathVariable long id) {
//        Optional<Task> task = taskService.getTaskById(id);
//        if (task.isEmpty()) {
//            taskNotFound(id);
//        } else
//            return task;
//        return task;
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable long id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task number " + id + " is not found."); // Возвращаем статус 404 и ваше сообщение
        } else {
            return ResponseEntity.ok(task.get()); // Возвращаем статус 200 OK и найденную задачу
        }
    }



    @DeleteMapping("/task/{taskId}")
    public void deleteTask(@PathVariable long taskId) {
        taskService.deleteById(taskId);
    }



}
