package com.example.forum.forum_api.dtos;

import com.example.forum.forum_api.enums.Roles;

public record UserRegisterDTO(String email, String name, String password) {

}
