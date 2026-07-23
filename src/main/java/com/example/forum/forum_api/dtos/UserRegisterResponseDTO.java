package com.example.forum.forum_api.dtos;

import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;

public record UserRegisterResponseDTO(Long id, String email, String name, Roles role) {

	public UserRegisterResponseDTO(User user) {
		this(user.getId(), user.getEmail(), user.getName(), user.getRole());
	}

}
