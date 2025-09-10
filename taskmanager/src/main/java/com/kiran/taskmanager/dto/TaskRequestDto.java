package com.kiran.taskmanager.dto;

import java.time.LocalDateTime;

import com.kiran.taskmanager.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequestDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "deadline cannot be empty")
    private LocalDateTime deadline;

    @NotNull(message = "assigneduserid cannot be empty")
    private Long assignedUserId;

    @NotNull(message = "task status cannot be empty")
    private TaskStatus status;
}
