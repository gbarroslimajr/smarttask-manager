package com.smarttask.service;

import com.smarttask.domain.entity.Project;
import com.smarttask.domain.entity.User;
import com.smarttask.dto.CreateProjectDTO;
import com.smarttask.dto.ProjectResponseDTO;
import com.smarttask.dto.UpdateProjectDTO;
import com.smarttask.exception.ProjectNotFoundException;
import com.smarttask.exception.UserNotFoundException;
import com.smarttask.mapper.ProjectMapper;
import com.smarttask.repository.ProjectRepository;
import com.smarttask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ProjectResponseDTO findById(UUID id) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectMapper.toDTO(project);
    }

    public ProjectResponseDTO create(CreateProjectDTO dto) {
        User user = userRepository.findById(dto.userId())
            .orElseThrow(() -> new UserNotFoundException(dto.userId()));

        Project project = ProjectMapper.toEntity(dto);
        project.setUser(user);

        Project savedProject = projectRepository.save(project);
        return ProjectMapper.toDTO(savedProject);
    }

    public ProjectResponseDTO update(UUID id, UpdateProjectDTO dto) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ProjectNotFoundException(id));

        ProjectMapper.updateEntityFromDTO(project, dto);

        // Hibernate automaticamente verifica version no commit
        // Se houver conflito, OptimisticLockException é lançada e propagada
        // O GlobalExceptionHandler captura e retorna HTTP 409
        Project updatedProject = projectRepository.save(project);
        return ProjectMapper.toDTO(updatedProject);
    }

    public void delete(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        projectRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> findByUserId(UUID userId, Pageable pageable) {
        return projectRepository.findByUserId(userId, pageable)
            .map(ProjectMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponseDTO> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable)
            .map(ProjectMapper::toDTO);
    }
}
