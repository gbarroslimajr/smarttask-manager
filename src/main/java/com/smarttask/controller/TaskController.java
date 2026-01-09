package com.smarttask.controller;

import com.smarttask.domain.enums.TaskStatus;
import com.smarttask.dto.CreateTaskDTO;
import com.smarttask.dto.TaskResponseDTO;
import com.smarttask.dto.UpdateTaskDTO;
import com.smarttask.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Tasks", description = "Operações de gerenciamento de tarefas")
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
        summary = "Criar nova tarefa",
        description = "Cria uma nova tarefa associada a um usuário e um projeto"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso",
            content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuário ou projeto não encontrado")
    })
    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody CreateTaskDTO dto) {
        TaskResponseDTO response = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Buscar tarefa por ID",
        description = "Retorna os dados de uma tarefa específica pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada",
            content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(
            @Parameter(description = "ID da tarefa", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        TaskResponseDTO response = taskService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Atualizar tarefa",
        description = "Atualiza os dados de uma tarefa existente. Utiliza optimistic locking para prevenir conflitos de concorrência."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = TaskResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "409", description = "Conflito de concorrência - a tarefa foi modificada por outro usuário")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(
            @Parameter(description = "ID da tarefa", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskDTO dto) {
        TaskResponseDTO response = taskService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Deletar tarefa",
        description = "Remove uma tarefa do sistema. Esta operação não pode ser desfeita."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da tarefa", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Listar todas as tarefas",
        description = "Retorna uma lista paginada de todas as tarefas do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> findAll(
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable) {
        Page<TaskResponseDTO> response = taskService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Listar tarefas por usuário",
        description = "Retorna uma lista paginada de todas as tarefas de um usuário específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TaskResponseDTO>> findByUserId(
            @Parameter(description = "ID do usuário", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID userId,
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable) {
        Page<TaskResponseDTO> response = taskService.findByUserId(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Listar tarefas por projeto",
        description = "Retorna uma lista paginada de todas as tarefas de um projeto específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    })
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Page<TaskResponseDTO>> findByProjectId(
            @Parameter(description = "ID do projeto", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID projectId,
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable) {
        Page<TaskResponseDTO> response = taskService.findByProjectId(projectId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Listar tarefas por usuário e status",
        description = "Retorna uma lista paginada de tarefas de um usuário filtradas por status (PENDING, IN_PROGRESS, COMPLETED, CANCELLED)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    })
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<Page<TaskResponseDTO>> findByUserIdAndStatus(
            @Parameter(description = "ID do usuário", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID userId,
            @Parameter(description = "Status da tarefa", required = true, example = "PENDING")
            @PathVariable TaskStatus status,
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable) {
        Page<TaskResponseDTO> response = taskService.findByUserIdAndStatus(userId, status, pageable);
        return ResponseEntity.ok(response);
    }
}
