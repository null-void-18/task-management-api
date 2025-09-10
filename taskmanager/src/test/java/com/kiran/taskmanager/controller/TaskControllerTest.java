package com.kiran.taskmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kiran.taskmanager.dto.TaskRequestDto;
import com.kiran.taskmanager.dto.TaskResponseDto;
import com.kiran.taskmanager.dto.UserResponseDto;
import com.kiran.taskmanager.enums.TaskStatus;
import com.kiran.taskmanager.service.TaskService;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TaskResponseDto sampleTaskResponse;
    private TaskRequestDto sampleTaskRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        objectMapper.registerModule(new JavaTimeModule()); 

        UserResponseDto userResponse = UserResponseDto.builder()
                .id(1L)
                .name("Kiran Sekhar")
                .email("kiran@example.com")
                .build();

        sampleTaskResponse = TaskResponseDto.builder()
                .id(1L)
                .title("Test Task")
                .description("Sample Task")
                .assignedUser(userResponse)
                .deadline(LocalDateTime.now().plusDays(1))
                .status(TaskStatus.TODO)
                .build();

        sampleTaskRequest = TaskRequestDto.builder()
                .title("Test Task")
                .description("Sample Task")
                .assignedUserId(1L)
                .deadline(LocalDateTime.now().plusDays(1))
                .status(TaskStatus.TODO)
                .build();
    }

    @Test
    void testGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(sampleTaskResponse));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void testGetTaskById() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(sampleTaskResponse);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void testCreateTask() throws Exception {
        when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(sampleTaskResponse);

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleTaskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void testUpdateTask() throws Exception {
        when(taskService.updateTask(eq(1L), any(TaskRequestDto.class))).thenReturn(sampleTaskResponse);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleTaskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
