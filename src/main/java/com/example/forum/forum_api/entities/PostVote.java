package com.example.forum.forum_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tb_post_vote", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "post_id", "user_id" })
})
public class PostVote extends Vote {

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	public PostVote() {
	}

	public PostVote(User voter, Boolean isUpvote, Post post) {
		super(voter, isUpvote);
		this.post = post;
	}

}
