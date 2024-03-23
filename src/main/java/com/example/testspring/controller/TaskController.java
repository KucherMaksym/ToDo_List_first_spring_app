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

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/{username}/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public String getOnlyName(Principal principal) {
        int startIndex = principal.getName().indexOf("username=") + "username=".length();
        int endIndex = principal.getName().indexOf(",", startIndex);
        return principal.getName().substring(startIndex, endIndex);
    }

//    @GetMapping("/all")
//    public List<Task> getAllTaskForUser(@PathVariable String username) {
//        AppUser user = userService.getUserByUsername(username);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUsername = getOnlyName(authentication);
//        if (!currentUsername.equals(username)) {
//            throw new AccessDeniedException("Доступ запрещен");
//        }
//        if (user == null) {
//            System.out.println("User with username " + username + " not found");
//        }
//
//        List<Task> userTasks = user.getUserTasks();
//        return userTasks;
//    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllTaskForUser(@PathVariable String username) {
        AppUser user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with username " + username + " not found");
        }
        List<Task> userTasks = user.getUserTasks();
        if (userTasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No tasks found for user " + username);
        }
        return ResponseEntity.ok(userTasks);
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

    @PatchMapping("/completed/{id}")// я делаю toggle isCompleted задачи не смотря на его статус. потом изменить
    public Task ToggleCompleted(@PathVariable Long id) {
        Optional<Task> optionalTask = taskService.getTaskById(id);
        Task task = null;
        if (optionalTask.isPresent()) {
            task = optionalTask.get();
            task.setCompleted(!task.isCompleted());
        }
        return taskService.updateTask(task);
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
            // Проверка, принадлежит ли задача указанному пользователю
            if (!task.get().getAppUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Task number " + taskId + " does not belong to user " + username);
            }
            return ResponseEntity.ok(task.get());
        }
    }



    @DeleteMapping("/delete/{taskId}")
    @CrossOrigin(origins = "http://localhost:3000")
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
