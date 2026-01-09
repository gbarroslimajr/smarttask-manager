package com.smarttask.service;

import com.smarttask.domain.entity.User;
import com.smarttask.dto.CreateUserDTO;
import com.smarttask.dto.UpdateUserDTO;
import com.smarttask.dto.UserResponseDTO;
import com.smarttask.exception.EmailAlreadyExistsException;
import com.smarttask.exception.UserNotFoundException;
import com.smarttask.mapper.UserMapper;
import com.smarttask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        return UserMapper.toDTO(user);
    }

    public UserResponseDTO create(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(dto.email());
        }

        User user = UserMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    public UserResponseDTO update(UUID id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new EmailAlreadyExistsException(dto.email());
            }
        }

        UserMapper.updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return UserMapper.toDTO(updatedUser);
    }

    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
            .map(UserMapper::toDTO);
    }
}
