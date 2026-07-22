package com.example.forum.forum_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.enums.PostStatus;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.CommentRepository;
import com.example.forum.forum_api.repositories.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

	@Autowired
	private CommentRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public Comment addComment(Comment comment) {

		User author = comment.getAuthor();
		Post post = comment.getPost();
		Forum forum = post.getForum();

		if (post.getIsLocked() || post.getIsDeleted()) {
			throw new DomainException("This post can't receive new comments");
		}

		if (post.getStatus().equals(PostStatus.PENDING)) {
			throw new DomainException("Peding posts can't receive comments");
		}

		if (forum.getBannedUsers().contains(author) || author.getIsBanned()) {
			throw new DomainException("Banned users can't comment");
		}

		Comment savedComment = repository.save(comment);
		return savedComment;
	}

	@Transactional
	public Comment addReply(Long originalCommentId, Comment comment) {

		Comment originalComment = repository.findById(originalCommentId)
				.orElseThrow(() -> new DomainException("Comment not found!"));

		User author = comment.getAuthor();
		Post post = originalComment.getPost();
		Forum forum = post.getForum();

		if (post.getIsLocked() || post.getIsDeleted()) {
			throw new DomainException("This post can't receive new comments");
		}

		if (originalComment.getIsDeleted()) {
			throw new DomainException("Deleted comments can't receive replies");
		}

		if (forum.getBannedUsers().contains(author) || author.getIsBanned()) {
			throw new DomainException("Banned users can't comment");
		}

		originalComment.addNestedComment(comment);
		comment.setPost(post);
		Comment savedComment = repository.save(comment);
		return savedComment;
	}

	@Transactional
	public void editComment(Long userId, Long commentId, String content) {

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (content == null || content.isBlank()) {
			throw new DomainException("Content can't be empty");
		}

		Comment comment = repository.findById(commentId)
				.orElseThrow(() -> new DomainException("Comment not found!"));

		User author = comment.getAuthor();

		if (!user.equals(author)) {
			throw new DomainException("You aren't allowed to edit this comment!");
		}

		comment.setText(content);
		repository.save(comment);
	}

	@Transactional
	public void deleteComment(Long userId, Long commentId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		Comment comment = repository.findById(commentId)
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
