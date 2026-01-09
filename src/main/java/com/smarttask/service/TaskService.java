package com.smarttask.service;

import com.smarttask.domain.entity.Project;
import com.smarttask.domain.entity.Task;
import com.smarttask.domain.entity.User;
import com.smarttask.domain.enums.TaskStatus;
import com.smarttask.dto.CreateTaskDTO;
import com.smarttask.dto.TaskResponseDTO;
import com.smarttask.dto.UpdateTaskDTO;
import com.smarttask.exception.ProjectNotFoundException;
import com.smarttask.exception.TaskNotFoundException;
import com.smarttask.exception.UserNotFoundException;
import com.smarttask.mapper.TaskMapper;
import com.smarttask.repository.ProjectRepository;
import com.smarttask.repository.TaskRepository;
import com.smarttask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO findById(UUID id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.toDTO(task);
    }

    public TaskResponseDTO create(CreateTaskDTO dto) {
        User user = userRepository.findById(dto.userId())
            .orElseThrow(() -> new UserNotFoundException(dto.userId()));

        Project project = projectRepository.findById(dto.projectId())
            .orElseThrow(() -> new ProjectNotFoundException(dto.projectId()));

        Task task = TaskMapper.toEntity(dto);
        task.setUser(user);
        task.setProject(project);

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDTO(savedTask);
    }

    public TaskResponseDTO update(UUID id, UpdateTaskDTO dto) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));

        TaskMapper.updateEntityFromDTO(task, dto);

        // Hibernate automaticamente verifica version no commit
        // Se houver conflito, OptimisticLockException é lançada e propagada
        // O GlobalExceptionHandler captura e retorna HTTP 409
        Task updatedTask = taskRepository.save(task);
        return TaskMapper.toDTO(updatedTask);
    }

    public void delete(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> findByUserId(UUID userId, Pageable pageable) {
        return taskRepository.findByUserId(userId, pageable)
            .map(TaskMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> findByProjectId(UUID projectId, Pageable pageable) {
        return taskRepository.findByProjectId(projectId, pageable)
            .map(TaskMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> findByUserIdAndStatus(UUID userId, TaskStatus status, Pageable pageable) {
        return taskRepository.findByUserIdAndStatus(userId, status, pageable)
            .map(TaskMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
            .map(TaskMapper::toDTO);
    }
}
