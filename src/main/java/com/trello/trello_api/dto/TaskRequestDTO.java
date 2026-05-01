package com.trello.trello_api.dto;

import com.trello.trello_api.entity.Priority;
import com.trello.trello_api.entity.Status;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TaskRequestDTO {
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private LocalDate createdAt;
}
