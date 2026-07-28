package com.example.forum.forum_api.dtos;

import com.example.forum.forum_api.entities.Forum;

public record ForumCreationResponseDTO(String name, String creatorName) {

	public ForumCreationResponseDTO(Forum forum) {
		this(forum.getName(), forum.getCreator().getName());
	}
}
