package com.smarttask.mapper;

import com.smarttask.domain.entity.Project;
import com.smarttask.dto.CreateProjectDTO;
import com.smarttask.dto.ProjectResponseDTO;
import com.smarttask.dto.UpdateProjectDTO;

public class ProjectMapper {

    public static ProjectResponseDTO toDTO(Project project) {
        return new ProjectResponseDTO(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getCreatedAt(),
            project.getUpdatedAt(),
            project.getUser() != null ? project.getUser().getId() : null,
            project.getVersion()
        );
    }

    public static Project toEntity(CreateProjectDTO dto) {
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        return project;
    }

    public static void updateEntityFromDTO(Project project, UpdateProjectDTO dto) {
        if (dto.name() != null) {
            project.setName(dto.name());
        }
        if (dto.description() != null) {
            project.setDescription(dto.description());
        }
    }
}
