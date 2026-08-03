package com.example.forum.forum_api.dtos;

import java.util.List;

public record PostDetailDTO(Long id, String title, String content, String posterName,
		String status, Boolean isLocked, List<CommentSummaryDTO> comments, long upvotes, long downvotes) {
}