package com.example.forum.forum_api.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tb_comment_vote", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "comment_id", "user_id" })
})
public class CommentVote extends Vote {

	@ManyToOne
	@JoinColumn(name = "comment_id", nullable = false)
	private Comment comment;

	public CommentVote() {
	}

	public CommentVote(User voter, Boolean isUpvote, Comment comment) {
		super(voter, isUpvote);
		this.comment = comment;
	}

}
