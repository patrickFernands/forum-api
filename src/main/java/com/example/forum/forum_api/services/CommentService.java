package com.example.forum.forum_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.CommentRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

	@Autowired
	private CommentRepository repository;

	@Transactional
	public Comment addComment(Comment comment) {

		User author = comment.getAuthor();
		Post post = comment.getPost();
		Forum forum = post.getForum();

		if (post.getIsLocked()) {
			throw new DomainException("Locked posts can't receive comments");
		}

		if (forum.getBannedUsers().contains(author)) {
			throw new DomainException("Banned users can't comment");
		}

		Comment savedComment = repository.save(comment);
		return savedComment;
	}

	@Transactional
	public Comment addReply(Long id, Comment comment) {

		Comment originalComment = repository.findById(id)
				.orElseThrow(() -> new DomainException("Comment not found!"));

		User author = comment.getAuthor();
		Post post = originalComment.getPost();
		Forum forum = post.getForum();

		if (post.getIsLocked()) {
			throw new DomainException("Locked posts can't receive comments");
		}

		if (forum.getBannedUsers().contains(author)) {
			throw new DomainException("Banned users can't comment");
		}

		originalComment.addNestedComment(comment);
		repository.save(originalComment);
		Comment savedComment = repository.save(comment);
		return savedComment;
	}

	@Transactional
	public void editComment(User user, Long id, String content) {

		if (content == null || content.isBlank()) {
			throw new DomainException("Content can't be empty");
		}

		Comment comment = repository.findById(id)
				.orElseThrow(() -> new DomainException("Comment not found!"));

		User author = comment.getAuthor();

		if (!user.equals(author)) {
			throw new DomainException("You aren't allowed to edit this comment!");
		}

		comment.setText(content);
		repository.save(comment);
	}

	@Transactional
	public void deleteComment(User user, Long id) {

		Comment comment = repository.findById(id)
				.orElseThrow(() -> new DomainException("Comment not found!"));

		User author = comment.getAuthor();
		Post post = comment.getPost();
		Forum forum = post.getForum();

		if (!user.equals(author) && !forum.getForumAdmins().contains(user) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to delete this comment!");
		}

		comment.setIsDeleted();
		repository.save(comment);
	}

}
