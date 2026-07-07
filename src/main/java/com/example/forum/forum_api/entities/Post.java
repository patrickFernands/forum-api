package com.example.forum.forum_api.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.Cache;

import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.exceptions.DomainException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_post")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "poster_id", nullable = false)
  private User poster;
  
  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @JsonIgnore
  @OneToMany(mappedBy = "postVote")
  private List<Vote> votes = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "postId")
  private List<Comment> comments;

  @Column(nullable = false)
  private Boolean isDeleted;

  public Post(User poster, String title, String content) throws DomainException {
    this.poster = poster;
    if (this.poster == null) {
      throw new DomainException("A post must have an author!");
    }
    this.title = title;
    if (this.title == null || this.title.equals("")) {
      throw new DomainException("A post must have a title!");
    }
    this.content = content;
    isDeleted = false;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(User user, String title) throws DomainException {
    if (!user.equals(poster)) {
      throw new DomainException("You aren't allowed to edit this post!");
    }
    if (title == null || title.equals("")) {
      throw new DomainException("Your post must have a valid title");
    }
    this.title = title;
  }

  public User getPoster() {
    return poster;
  }

  public String getContent() {
    return content;
  }

  public void setContent(User user, String content) throws DomainException {
    if (!user.equals(poster)) {
      throw new DomainException("You aren't allowed to edit this post!");
    }
    if (content == null || content.equals("")) {
      throw new DomainException("Your post must have content!");
    }
    this.content = content;
  }

  public void addComment(Comment comment, Forum forum) throws DomainException {

    User author = comment.getAuthor();

    if (author.getIsBanned() || forum.getBannedUsers().contains(author)) {
      throw new DomainException("You aren't allowed to comment!");
    }
    comment.setPostId(this);
    comments.add(comment);
  }

  public void removeComment(User user, Integer commentId, Forum forum) throws DomainException {
    Comment comment = comments.get(commentId);
    if (comment == null) {
      throw new DomainException("Comment not found!");
    }
    if (!comment.getAuthor().equals(user) && user.getRole().equals(Roles.ADMIN)
        && !forum.getForumAdmins().contains(user)) {
      throw new DomainException("You aren't allowed to delete this comment!");
    }
    comment.setIsDeleted();
  }

  public void voteInComment(User user, Integer commentId, Boolean isUpvote, Forum forum) throws DomainException {

    Comment comment = comments.get(commentId);

    if (comment == null) {
      throw new DomainException("Comment not found");
    }

    comment.addVote(user, isUpvote, forum);
  }

  public void addNestedComment(Integer upperCommentId, Comment commentToAdd, Forum forum) throws DomainException {

    Comment upperComment = comments.get(upperCommentId);
    User author = commentToAdd.getAuthor();

    if (author.getIsBanned() || forum.getBannedUsers().contains(author)) {
      throw new DomainException("You aren't allowed to comment!");
    }
    if (upperComment == null) {
      throw new DomainException("Comment not found!");
    }

    commentToAdd.setParentComment(upperComment);
    comments.get(upperCommentId).addNestedComment(commentToAdd);
  }

  public List<Comment> getComments() {
    return Collections.unmodifiableList(comments);
  }

  public void addVote(User user, Boolean voteType, Forum forum) throws DomainException {
    if (voteType == null) {
      throw new DomainException("Vote value cannot be null");
    }
    if (user.getIsBanned() || forum.getBannedUsers().contains(user)) {
      throw new DomainException("Banned users can't vote");
    }
    votes.add(new Vote(user, voteType, this));
  }

  public List<Vote> getVotes() {
    return Collections.unmodifiableList(votes);
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted() throws DomainException {
    isDeleted = true;
    setTitle(poster, "Deleted");
    setContent(poster, "This post was deleted!");
    votes.clear();
    comments.clear();
  }

}
