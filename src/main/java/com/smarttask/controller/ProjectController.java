package com.smarttask.controller;

import com.smarttask.dto.CreateProjectDTO;
import com.smarttask.dto.ProjectResponseDTO;
import com.smarttask.dto.UpdateProjectDTO;
import com.smarttask.service.ProjectService;
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

@Tag(name = "Projects", description = "Operações de gerenciamento de projetos")
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
        summary = "Criar novo projeto",
        description = "Cria um novo projeto associado a um usuário"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(@Valid @RequestBody CreateProjectDTO dto) {
        ProjectResponseDTO response = projectService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Buscar projeto por ID",
        description = "Retorna os dados de um projeto específico pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projeto encontrado",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> findById(
            @Parameter(description = "ID do projeto", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        ProjectResponseDTO response = projectService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Atualizar projeto",
        description = "Atualiza os dados de um projeto existente. Utiliza optimistic locking para prevenir conflitos de concorrência."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = ProjectResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflito de concorrência - o projeto foi modificado por outro usuário")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(
            @Parameter(description = "ID do projeto", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectDTO dto) {
        ProjectResponseDTO response = projectService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Deletar projeto",
        description = "Remove um projeto do sistema. Esta operação não pode ser desfeita."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Projeto deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do projeto", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Listar todos os projetos",
        description = "Retorna uma lista paginada de todos os projetos do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de projetos retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<ProjectResponseDTO>> findAll(
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable) {
        Page<ProjectResponseDTO> response = projectService.findAll(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Listar projetos por usuário",
        description = "Retorna uma lista paginada de todos os projetos de um usuário específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de projetos retornada com sucesso")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ProjectResponseDTO>> findByUserId(
            @Parameter(description = "ID do usuário", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID userId,
            @Parameter(description = "Parâmetros de paginação (page, size, sort)")
            Pageable pageable) {
        Page<ProjectResponseDTO> response = projectService.findByUserId(userId, pageable);
        return ResponseEntity.ok(response);
    }
}
