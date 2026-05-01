package com.trello.trello_api.controller;

import com.trello.trello_api.dto.PagedResponseDTO;
import com.trello.trello_api.dto.TaskRequestDTO;
import com.trello.trello_api.dto.TaskResponseDTO;
import com.trello.trello_api.entity.Priority;
import com.trello.trello_api.entity.Status;
import com.trello.trello_api.entity.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.trello.trello_api.service.TaskService;

@RestController
@RequestMapping("api/task")
@Tag(name = "Trello task", description = "Operações sobre tarefas")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping("/create")
    @Operation(summary = "Criar uma nova tarefa")
    public ResponseEntity<TaskResponseDTO> create (@Valid @RequestBody TaskRequestDTO dto){
        TaskResponseDTO created = service.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/list")
    @Operation(summary = "Listar todas as tarefas")
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> list (Pageable pageable){
        return ResponseEntity.ok(service.list(pageable));
    }

    @GetMapping("/list/{Id}")
    @Operation(summary = "Listar task específica através do ID")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long Id){
        return ResponseEntity.ok(service.findById(Id));
    }

    @PutMapping("/update/{Id}")
    @Operation(summary = "Atualizar task específica através do ID")
    public ResponseEntity<TaskResponseDTO> update (@PathVariable Long Id, @Valid @RequestBody TaskRequestDTO dto){
        return ResponseEntity.ok(service.updateTask(Id, dto));
    }

    @DeleteMapping("/delete/{Id}")
    @Operation(summary = "Apagar tarefa específica através do Id")
    public String delete(@PathVariable Long Id){
        service.deleteTask(Id);
        return "Task " + Id + " deleted with success!!!";
    }

    @GetMapping("concluded/{Id}")
    @Operation(summary = "Marcar tarefa específica como concluída")
    public ResponseEntity<TaskResponseDTO> markAsConcluded(@PathVariable Long Id){
        return ResponseEntity.ok(service.markAsConcluded(Id));
    }

    @GetMapping("/listAll/status")
    @Operation(summary = "Listar as tasks por status")
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> listByStatus (@RequestParam Status status, Pageable pageable){
        return ResponseEntity.ok(service.listByStatus(status, pageable));
    }

    @GetMapping("/listAll/priority")
    @Operation(summary = "Listar as tasks por prioridade")
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> listByStatus (@RequestParam Priority priority, Pageable pageable){
        return ResponseEntity.ok(service.listByPriority(priority, pageable));
    }
}
