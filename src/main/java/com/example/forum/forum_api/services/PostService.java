package com.example.forum.forum_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.PostRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository repository;

	@Transactional
	public Post addPost(Post post) {

		if (post.getTitle() == null || post.getTitle().isBlank()) {
			throw new DomainException("Title can't be empty");
		}

		if (post.getContent() == null || post.getContent().isBlank()) {
			throw new DomainException("Content can't be empty");
		}

		User poster = post.getPoster();
		Forum forum = post.getForum();

		if (forum.getBannedUsers().contains(poster) || poster.getIsBanned()) {
			throw new DomainException("Banned users can't post");
		}

		Post savedPost = repository.save(post);
		return savedPost;
	}

	@Transactional
	public void editTitle(User user, Long id, String title) {

		if (title == null || title.isBlank()) {
			throw new DomainException("Title can't be empty");
		}

		Post post = repository.findById(id)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();

		if (!user.equals(poster)) {
			throw new DomainException("You aren't allowed to edit the title");
		}

		post.setTitle(title);
		repository.save(post);
	}

	@Transactional
	public void editContent(User user, Long id, String content) {

		if (content == null || content.isBlank()) {
			throw new DomainException("Content can't be empty");
		}

		Post post = repository.findById(id)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();

		if (!user.equals(poster)) {
			throw new DomainException("You aren't allowed to edit this post!");
		}

		post.setContent(content);
		repository.save(post);
	}

	@Transactional
	public void lockPost(User user, Long id) {

		Post post = repository.findById(id)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();
		Forum forum = post.getForum();

		if (!user.equals(poster) && !forum.getForumAdmins().contains(user) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to lock this post!");
		}

		post.lock();
		repository.save(post);
	}

	@Transactional
	public void unlockPost(User user, Long id) {

		Post post = repository.findById(id)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();
		Forum forum = post.getForum();

		if (!user.equals(poster) && !forum.getForumAdmins().contains(user) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to unlock this post!");
		}

		post.unlock();
		repository.save(post);
	}

	@Transactional
	public void deletePost(User user, Long id) {

		Post post = repository.findById(id)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();
		Forum forum = post.getForum();

		if (!user.equals(poster) && !forum.getForumAdmins().contains(user) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to delete this post!");
		}

		post.setIsDeleted();
		repository.save(post);
	}

}
