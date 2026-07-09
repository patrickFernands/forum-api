package com.example.forum.forum_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_post_vote")
public class PostVote extends Vote {

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	public PostVote(User voter, Boolean isUpvote, Post post) {
		super(voter, isUpvote);
		this.post = post;
	}

}
