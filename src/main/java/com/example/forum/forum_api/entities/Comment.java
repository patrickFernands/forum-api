package com.example.forum.forum_api.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
@Table(name = "tb_comment")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String text;

  @ManyToOne
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @ManyToOne
  @JoinColumn(name = "parent_comment_id")
  private Comment parentComment;

  @OneToMany(mappedBy = "parentComment")
  private List<Comment> nestedComments = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "comment")
  private List<CommentVote> votes = new ArrayList<>();

  @Column(nullable = false)
  private Boolean isDeleted;

  public Comment() {

  }

  public Comment(User author, String text, Post post) {
    this.author = author;
    this.text = text;
    this.post = post;
    isDeleted = false;
  }

  public Long getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public User getAuthor() {
    return author;
  }

  public void addNestedComment(Comment nestedComment) {
    nestedComment.parentComment = this;
    nestedComments.add(nestedComment);
  }

  public void removeNestedComment(Comment nestedComment) {
    nestedComment.setIsDeleted();
  }

  public List<Comment> getCommentNest() {
    return Collections.unmodifiableList(nestedComments);
  }

  public void addVote(CommentVote vote) {
    votes.add(vote);
  }

  public List<CommentVote> getVotes() {
    return Collections.unmodifiableList(votes);
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted() {
    this.setText("Comment was deleted.");
    this.isDeleted = true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  public Comment getParentComment() {
    return parentComment;
  }

  public void setParentComment(Comment parentComment) {
    this.parentComment = parentComment;
  }

  public Post getPost() {
    return post;
  }

  public void setPost(Post post) {
    this.post = post;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Comment other = (Comment) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
