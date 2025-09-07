package com.kiran.taskmanager.dto;

import java.time.LocalDateTime;

import com.kiran.taskmanager.enums.TaskStatus;

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
    private String title;

    private String description;

    private LocalDateTime deadline;

    private Long assignedUserId;

    private TaskStatus status;
}
