package com.kiran.taskmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiran.taskmanager.dto.TaskRequestDto;
import com.kiran.taskmanager.dto.TaskResponseDto;
import com.kiran.taskmanager.exception.TaskNotFoundException;
import com.kiran.taskmanager.service.TaskService;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    // ----------------------------
    // Authentication tests
    // ----------------------------

    @Test
    public void whenNoAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/tasks/1"))
               .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/tasks")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{}"))
               .andExpect(status().isUnauthorized());
    }

    // ----------------------------
    // GET /tasks/{id}
    // ----------------------------

    @Test
    @WithMockUser(username = "user1")
    public void whenGetTask_thenOk() throws Exception {
        TaskResponseDto task = TaskResponseDto.builder()
                .id(1L)
                .title("Sample Task")
                .description("Desc")
                .deadline(LocalDateTime.now().plusDays(1))
                .build();

        Mockito.when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Sample Task"));
    }

    @Test
    @WithMockUser
    public void whenGetNonExistentTask_thenNotFound() throws Exception {
        Mockito.when(taskService.getTaskById(999L))
               .thenThrow(new TaskNotFoundException("Task not found with id: 999"));

        mockMvc.perform(get("/api/tasks/999"))
               .andExpect(status().isNotFound());
    }

    // ----------------------------
    // GET /tasks
    // ----------------------------

    @Test
    @WithMockUser
    public void whenGetAllTasks_thenOk() throws Exception {
        TaskResponseDto task1 = TaskResponseDto.builder().id(1L).title("T1").build();
        TaskResponseDto task2 = TaskResponseDto.builder().id(2L).title("T2").build();

        Mockito.when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        mockMvc.perform(get("/api/tasks"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2));
    }

    // ----------------------------
    // POST /tasks
    // ----------------------------

    @Test
    @WithMockUser
    public void whenCreateTaskValid_thenOk() throws Exception {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("New Task");
        request.setDescription("Desc");
        request.setDeadline(LocalDateTime.now().plusDays(1));
        request.setAssignedUserId(1L);

        TaskResponseDto response = TaskResponseDto.builder()
                .id(1L)
                .title("New Task")
                .description("Desc")
                .deadline(request.getDeadline())
                .build();

        Mockito.when(taskService.createTask(any(TaskRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/tasks")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    @WithMockUser
    public void whenCreateTaskInvalid_thenBadRequest() throws Exception {
        TaskRequestDto request = new TaskRequestDto(); // missing required fields

        mockMvc.perform(post("/api/tasks")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest());
    }

    // ----------------------------
    // PUT /tasks/{id}
    // ----------------------------

    @Test
    @WithMockUser
    public void whenUpdateTask_thenOk() throws Exception {
        TaskRequestDto request = new TaskRequestDto();
        request.setTitle("Updated Task");
        request.setDescription("Desc Updated");
        request.setDeadline(LocalDateTime.now().plusDays(2));
        request.setAssignedUserId(1L);

        TaskResponseDto response = TaskResponseDto.builder()
                .id(1L)
                .title("Updated Task")
                .build();

        Mockito.when(taskService.updateTask(eq(1L), any(TaskRequestDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/tasks/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    // ----------------------------
    // DELETE /tasks/{id}
    // ----------------------------

    @Test
    @WithMockUser
    public void whenDeleteTask_thenOk() throws Exception {
        Mockito.doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
            .andExpect(status().isOk());

        Mockito.verify(taskService).deleteTask(1L);
    }
}
