package com.example.forum.forum_api.dtos;

public record NewCommentDTO(Long authorId, String text, Long postId) {

}
