package com.trello.trello_api.repository;

import com.trello.trello_api.entity.Priority;
import com.trello.trello_api.entity.Status;
import com.trello.trello_api.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long > {

    // save()
    // findAll()
    // findById()
    // delete()

    Page<Task> findTaskByStatus(Status status, Pageable pageable);

    Page<Task> findTasksByPriority(Priority priority, Pageable pageable);
}
