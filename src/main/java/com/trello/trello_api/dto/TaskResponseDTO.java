package com.trello.trello_api.dto;

import com.trello.trello_api.entity.Priority;
import com.trello.trello_api.entity.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TaskResponseDTO {
    private Long Id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private LocalDate createdAt;
}
