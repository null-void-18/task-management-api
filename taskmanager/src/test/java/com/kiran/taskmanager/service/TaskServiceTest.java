package com.kiran.taskmanager.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.kiran.taskmanager.dto.TaskRequestDto;
import com.kiran.taskmanager.exception.TaskNotFoundException;
import com.kiran.taskmanager.exception.UserNotFoundException;
import com.kiran.taskmanager.model.Task;
import com.kiran.taskmanager.model.User;
import com.kiran.taskmanager.repository.TaskRepository;
import com.kiran.taskmanager.repository.UserRepository;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService; // needed for mapToDto

    @InjectMocks
    private TaskService taskService;

    private User sampleUser;
    private Task sampleTask;
    private TaskRequestDto sampleRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleUser = User.builder()
                .id(1L)
                .name("Kiran Sekhar")
                .email("kiran@example.com")
                .build();

        sampleTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Sample Task")
                .assignedUser(sampleUser)
                .deadline(LocalDateTime.now().plusDays(1))
                .build();

        sampleRequest = TaskRequestDto.builder()
                .title("Test Task")
                .description("Sample Task")
                .assignedUserId(1L)
                .deadline(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    void testCreateTask_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        assertNotNull(taskService.createTask(sampleRequest));

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testCreateTask_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            taskService.createTask(sampleRequest);
        });
    }

    @Test
    void testDeleteTask_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).delete(sampleTask);
    }

    @Test
    void testDeleteTask_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(1L));
    }
}
