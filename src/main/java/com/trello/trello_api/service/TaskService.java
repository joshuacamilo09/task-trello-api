package com.trello.trello_api.service;

import com.trello.trello_api.dto.PagedResponseDTO;
import com.trello.trello_api.dto.TaskRequestDTO;
import com.trello.trello_api.dto.TaskResponseDTO;
import com.trello.trello_api.entity.Priority;
import com.trello.trello_api.entity.Status;
import com.trello.trello_api.entity.Task;
import com.trello.trello_api.exception.ResourceNotFoundException;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.trello.trello_api.repository.TaskRepository;
import java.time.LocalDate;

@Service
@Builder
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository = repository;
    }

    public TaskResponseDTO createTask(TaskRequestDTO dto){
        log.info("Creating new task!");

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .priority(dto.getPriority())
                .dueDate(dto.getDueDate())
                .createdAt(LocalDate.now()) //campos fixos aqui também
                .build();

        Task saved = repository.save(task);

        return toResponseDTO(saved);
    }

    public PagedResponseDTO<TaskResponseDTO> list (Pageable pageable) {
        log.debug("A listar despesas!");
        Page<Task> page = repository.findAll(pageable);
        return getTaskResponseDTOpagedResponseDTO(page);
    }

    public TaskResponseDTO findById(Long id){
        log.debug("A encontrar task por id ");
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa com id: " + id + " não encontrada"));
        return toResponseDTO(task);
    }

    public TaskResponseDTO updateTask(Long Id, TaskRequestDTO dto){
        log.info("A atualizar despesa!");

        Task task = repository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + Id + " not found!"));

        if(dto.getTitle() != null){
            task.setTitle(dto.getTitle());
        }
        if(dto.getDescription() != null){
            task.setDescription(dto.getDescription());
        }
        if(dto.getStatus() != null){
            task.setStatus(dto.getStatus());
        }
        if(dto.getPriority()!= null){
            task.setPriority(dto.getPriority());
        }
        if(dto.getDueDate() != null){
            task.setDueDate(dto.getDueDate());
        }

        return toResponseDTO(repository.save(task));
    }

    public TaskResponseDTO markAsConcluded(Long Id){
        log.debug("Marcar tarefa como concluida");
        Task task = repository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + Id + " not found!"));
        task.setStatus(Status.DONE);
        return toResponseDTO(repository.save(task));
    }

    public PagedResponseDTO<TaskResponseDTO> listByStatus(Status status, Pageable pageable){
        log.debug("Filtrar por status");
        Page<Task> page = repository.findTaskByStatus(status, pageable);
        return getTaskResponseDTOpagedResponseDTO(page);
    }

    public PagedResponseDTO<TaskResponseDTO> listByPriority(Priority priority, Pageable pageable){
        log.debug("Filtrar por prioridade");
        Page<Task> page = repository.findTasksByPriority(priority, pageable);
        return getTaskResponseDTOpagedResponseDTO(page);
    }

    public void deleteTask(Long id) {
        log.info("A apagar tarefa");
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));
        repository.delete(task);
    }

    //helpers
    private TaskResponseDTO toResponseDTO(Task task){
        return TaskResponseDTO.builder()
                .Id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .build();
    }

    private PagedResponseDTO<TaskResponseDTO> getTaskResponseDTOpagedResponseDTO(Page<Task> pageResult){
        return PagedResponseDTO.<TaskResponseDTO>builder()
                .content(pageResult.getContent().stream()
                        .map(this::toResponseDTO)
                        .toList()
                )
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .last(pageResult.isLast())
                .build();
    }
}
