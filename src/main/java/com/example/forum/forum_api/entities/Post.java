package com.example.forum.forum_api.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.forum.forum_api.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_post")
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
  private List<Comment> comments = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "forum_id")
  private Forum forum;

  @Enumerated(EnumType.STRING)
  private PostStatus status = PostStatus.PENDING;

  @Column(nullable = false)
  private Boolean isDeleted;

  public Post() {

  }

  public Post(User poster, String title, String content) {
    this.poster = poster;
    this.title = title;
    this.content = content;
    isDeleted = false;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public PostStatus getStatus() {
    return status;
  }

  public void setStatus(PostStatus status) {
    this.status = status;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public User getPoster() {
    return poster;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void addComment(Comment comment) {
    comment.setPost(this);
    comments.add(comment);
  }

  public void removeComment(Comment comment) {
    comment.setIsDeleted();
  }

  public void voteInComment(Comment comment, Vote vote) {
    comment.addVote(vote);
  }

  public void addNestedComment(Comment upperComment, Comment commentToAdd) {
    commentToAdd.setParentComment(upperComment);
    upperComment.addNestedComment(commentToAdd);
  }

  public List<Comment> getComments() {
    return Collections.unmodifiableList(comments);
  }

  public void addVote(Vote vote) {
    votes.add(vote);
  }

  public List<Vote> getVotes() {
    return Collections.unmodifiableList(votes);
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted() {
    isDeleted = true;
    setTitle("Deleted");
    setContent("This post was deleted!");
  }

}
