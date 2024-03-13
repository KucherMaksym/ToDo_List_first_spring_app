package com.example.testspring.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.testspring.model.AppUser;
import com.example.testspring.model.Task;
import com.example.testspring.service.TaskService;
import com.example.testspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/{username}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<Task> getAllTaskForUser(@PathVariable String username) {
        AppUser user = userService.getUserByUsername(username);


        //получаем имя аутентифицированного юзера. если оно не равно
        //имени, указанным в url, на который он пытается попасть,
        //выкидывается exception 🔱🔱🔱
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        if (!currentUsername.equals(username)) {
            throw new AccessDeniedException("Доступ запрещен");
        }
        if (user == null) {
            System.out.println("User with username " + username + " not found");
        }

        List<Task> userTasks = user.getUserTasks();
        return userTasks;
    }

    @PostMapping("/create")
    public Task createTask(@PathVariable String username, @RequestBody Task task) {
        AppUser user = userService.getUserByUsername(username);
        if (user == null) {
            System.out.println("User with username " + username + " not found");
        }
        task.setAppUser(user);
        Task newTask = taskService.createTask(task);
        return newTask;
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable String username, @PathVariable long taskId) {
        AppUser user = userService.getUserByUsername(username);
        if (user == null) {
            System.out.println("User with username " + username + " not found");
        }
        Optional<Task> task = taskService.getTaskById(taskId);
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task number " + taskId + " is not found for user " + username);
        } else {
            // Проверяем, принадлежит ли задача указанному пользователю
            if (!task.get().getAppUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Task number " + taskId + " does not belong to user " + username);
            }
            return ResponseEntity.ok(task.get());
        }
    }



    @DeleteMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable String username, @PathVariable long taskId) {
        AppUser user = userService.getUserByUsername(username);
        if (user == null) {
            System.out.println("User with username " + username + " not found");
        }
        Task taskToDelete = taskService.getTaskById(taskId).orElseThrow(() ->
                new RuntimeException("Task with id " + taskId + " not found"));
        if (!taskToDelete.getAppUser().equals(user)) {
            return "task number " + taskId + " doesnt belong to user";
        }
        taskService.deleteById(taskId);
        return taskId + " task has been deleted";
    }



}
