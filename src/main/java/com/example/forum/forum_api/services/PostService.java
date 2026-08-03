package com.example.forum.forum_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forum.forum_api.dtos.CommentSummaryDTO;
import com.example.forum.forum_api.dtos.PostDetailDTO;
import com.example.forum.forum_api.dtos.PostSummaryDTO;
import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.PostStatus;
import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.CommentRepository;
import com.example.forum.forum_api.repositories.CommentVoteRepository;
import com.example.forum.forum_api.repositories.ForumRepository;
import com.example.forum.forum_api.repositories.PostRepository;
import com.example.forum.forum_api.repositories.PostVoteRepository;
import com.example.forum.forum_api.repositories.UserRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ForumRepository forumRepository;

	@Autowired
	private PostVoteRepository postVoteRepository;

	@Autowired
	private CommentVoteRepository commentVoteRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Transactional
	public Post addPost(Long posterId, Long forumId, String title, String content) {

		User poster = userRepository.findById(posterId)
				.orElseThrow(() -> new DomainException("User not found!"));

		Forum forum = forumRepository.findById(forumId)
				.orElseThrow(() -> new DomainException("Forum not found!"));

		if (title == null || title.isBlank()) {
			throw new DomainException("Title can't be empty");
		}

		if (content == null || content.isBlank()) {
			throw new DomainException("Content can't be empty");
		}

		if (forum.getIsDeleted()) {
			throw new DomainException("Deleted forums can't receive new posts");
		}

		if (forum.getBannedUsers().contains(poster) || poster.getIsBanned()) {
			throw new DomainException("Banned users can't post");
		}

		Post post = new Post(poster, forum, title, content);
		Post savedPost = repository.save(post);
		return savedPost;
	}

	@Transactional
	public void editTitle(Long userId, Long postId, String title) {

		if (title == null || title.isBlank()) {
			throw new DomainException("Title can't be empty");
		}

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		Post post = repository.findById(postId)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();

		if (!user.equals(poster)) {
			throw new DomainException("You aren't allowed to edit the title");
		}

		post.setTitle(title);
		repository.save(post);
	}

	@Transactional
	public void editContent(Long userId, Long postId, String content) {

		if (content == null || content.isBlank()) {
			throw new DomainException("Content can't be empty");
		}

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		Post post = repository.findById(postId)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();

		if (!user.equals(poster)) {
			throw new DomainException("You aren't allowed to edit this post!");
		}

		post.setContent(content);
		repository.save(post);
	}

	@Transactional
	public void lockPost(Long userId, Long postId) {

		Post post = repository.findById(postId)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();
		Forum forum = post.getForum();

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (!user.equals(poster) && !forum.getForumAdmins().contains(user) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to lock this post!");
		}

		post.lock();
		repository.save(post);
	}

	@Transactional
	public void unlockPost(Long userId, Long postId) {

		Post post = repository.findById(postId)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();
		Forum forum = post.getForum();

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (!user.equals(poster) && !forum.getForumAdmins().contains(user) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to unlock this post!");
		}

		post.unlock();
		repository.save(post);
	}

	@Transactional
	public void deletePost(Long userId, Long postId) {

		Post post = repository.findById(postId)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User poster = post.getPoster();
		Forum forum = post.getForum();

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (!user.equals(poster) && !forum.getForumAdmins().contains(user) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to delete this post!");
		}

		post.setIsDeleted();
		repository.save(post);
	}

	@Transactional
	public void approvePost(Long userId, Long postId) {

		Post post = repository.findById(postId)
				.orElseThrow(() -> new DomainException("Post not found!"));

		Forum forum = post.getForum();

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (!forum.getForumAdmins().contains(user) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to approve this post!");
		}

		post.setStatus(PostStatus.APPROVED);
		repository.save(post);
	}

	public List<PostSummaryDTO> getPostsByForum(Long forumId) {

		Forum forum = forumRepository.findById(forumId)
				.orElseThrow(() -> new DomainException("Forum not found"));

		return repository.findByForumAndIsDeletedFalse(forum).stream()
				.map(p -> new PostSummaryDTO(p.getId(), p.getTitle(), p.getPoster().getName(),
						p.getStatus().toString(), p.getIsLocked()))
				.toList();
	}

	public PostDetailDTO getPostById(Long id) {
		Post post = repository.findById(id)
				.orElseThrow(() -> new DomainException("Post not found"));

		long upvotes = postVoteRepository.countByPostAndIsUpvoteTrue(post);
		long downvotes = postVoteRepository.countByPostAndIsUpvoteFalse(post);

		List<CommentSummaryDTO> comments = post.getComments().stream().map(this::toDTO).toList();

		return new PostDetailDTO(post.getId(), post.getTitle(), post.getContent(), post.getPoster().getName(),
				post.getStatus().toString(), post.getIsLocked(), comments, upvotes, downvotes);
	}

	public List<PostSummaryDTO> searchPosts(String query) {
		return repository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(query).stream()
				.map(p -> new PostSummaryDTO(p.getId(), p.getTitle(), p.getPoster().getName(),
						p.getStatus().toString(), p.getIsLocked()))
				.toList();
	}

	// método pra auxiliar na obtenção dos comentários aninhados.
	private CommentSummaryDTO toDTO(Comment c) {

		return new CommentSummaryDTO(c.getId(), c.getText(), c.getAuthor().getName(),
				commentVoteRepository.countByCommentAndIsUpvoteTrue(c), commentVoteRepository.countByCommentAndIsUpvoteFalse(c),
				c.getNestedComments().stream().map(a -> toDTO(a)).toList());
	}

}
