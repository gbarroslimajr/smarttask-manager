package com.smarttask.mapper;

import com.smarttask.domain.entity.User;
import com.smarttask.dto.CreateUserDTO;
import com.smarttask.dto.UpdateUserDTO;
import com.smarttask.dto.UserResponseDTO;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            null // createdAt não está na entidade User atual
        );
    }

    public static User toEntity(CreateUserDTO dto) {
        return new User(dto.name(), dto.email());
    }

    public static void updateEntityFromDTO(User user, UpdateUserDTO dto) {
        if (dto.name() != null) {
            user.setName(dto.name());
        }
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
    }
}
