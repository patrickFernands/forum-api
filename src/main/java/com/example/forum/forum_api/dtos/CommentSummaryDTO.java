package com.example.forum.forum_api.dtos;

import java.util.List;

public record CommentSummaryDTO(Long id, String text, String authorName, long upvotes, long downvotes,
		List<CommentSummaryDTO> replies) {
}
