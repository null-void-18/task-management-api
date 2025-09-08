package com.kiran.taskmanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kiran.taskmanager.dto.TaskRequestDto;
import com.kiran.taskmanager.dto.TaskResponseDto;
import com.kiran.taskmanager.dto.UserResponseDto;
import com.kiran.taskmanager.exception.TaskNotFoundException;
import com.kiran.taskmanager.exception.UserNotFoundException;
import com.kiran.taskmanager.model.Task;
import com.kiran.taskmanager.model.User;
import com.kiran.taskmanager.repository.TaskRepository;
import com.kiran.taskmanager.repository.UserRepository;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }



    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        Task task = mapToEntity(taskRequestDto);
        Task savedTask = taskRepository.save(task);

        return mapToDto(savedTask);
    }

    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found with id:" + id));
        
        return mapToDto(task);
    }

    public List<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        
        List<TaskResponseDto> savedTasks = new ArrayList<>();

        for(Task task : tasks) {
            savedTasks.add(mapToDto(task));
        }

        return savedTasks;
    }

    public TaskResponseDto updateTask(Long id,TaskRequestDto taskRequestDto) {
        Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found with id:" + id));
        
        User user = userRepository.findById(taskRequestDto.getAssignedUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + taskRequestDto.getAssignedUserId()));
        
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setDeadline(taskRequestDto.getDeadline());
        task.setUpdatedAt(LocalDateTime.now());
        task.setStatus(taskRequestDto.getStatus());
        task.setAssignedUser(user);

        Task savedTask = taskRepository.save(task);

        return mapToDto(savedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new TaskNotFoundException("Task not found with id:" + id));

        taskRepository.delete(task);
    }


    public Task mapToEntity(TaskRequestDto taskRequestDto) {
        User user = userRepository.findById(taskRequestDto.getAssignedUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + taskRequestDto.getAssignedUserId()));


        return Task.builder()
                .title(taskRequestDto.getTitle())
                .description(taskRequestDto.getDescription())
                .assignedUser(user)
                .deadline(taskRequestDto.getDeadline())
                .status(taskRequestDto.getStatus()).build();
    }



    public TaskResponseDto mapToDto(Task task) {
        UserResponseDto userResponseDto = userService.mapToDto(task.getAssignedUser());

        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .assignedUser(userResponseDto)
                .deadline(task.getDeadline())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt()).build();
    }
}