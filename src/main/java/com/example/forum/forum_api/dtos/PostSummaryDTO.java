package com.example.forum.forum_api.dtos;

public record PostSummaryDTO(Long id, String title, String posterName, String status, Boolean isLocked) {
}