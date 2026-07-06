package com.example.forum.forum_api.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.example.forum.forum_api.exceptions.DomainException;

public class Post {

  private static Integer idCount = 1;
  private User author;
  private String title;
  private String content;
  private Map<Integer, Boolean> votes;
  private Map<Integer, Comment> comments;
  private Integer id;
  private Boolean isDeleted;

  public Post(User author, String title, String content) throws DomainException {
    this.author = author;
    if (this.author == null) {
      throw new DomainException("A post must have an author!");
    }
    this.title = title;
    if (this.title == null || this.title.equals("")) {
      throw new DomainException("A post must have a title!");
    }
    this.content = content;
    if (this.content == null || this.content.equals("")) {
      throw new DomainException("A post must have content!");
    }
    id = idCount;
    idCount++;
    votes = new HashMap<>();
    comments = new HashMap<>();
    isDeleted = false;
  }

  public Integer getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(User user, String title) throws DomainException {
    if (!user.equals(author)) {
      throw new DomainException("You aren't allowed to edit this post!");
    }
    if (title == null || title.equals("")) {
      throw new DomainException("Your post must have a valid title");
    }
    this.title = title;
  }

  public User getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  public void setContent(User user, String content) throws DomainException {
    if (!user.equals(author)) {
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
    comments.put(comment.getId(), comment);
  }

  public void removeComment(User user, Integer commentId, Forum forum) throws DomainException {
    Comment comment = comments.get(commentId);
    if (comment == null) {
      throw new DomainException("Comment not found!");
    }
    if (!comment.getAuthor().equals(user) && !user.getIsAdmin()
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

    comments.get(upperCommentId).addNestedComment(commentToAdd);
    comments.put(commentToAdd.getId(), commentToAdd);
  }

  public Map<Integer, Comment> getComments() {
    return Collections.unmodifiableMap(comments);
  }

  public void addVote(User user, Boolean voteType, Forum forum) throws DomainException {
    if (voteType == null) {
      throw new DomainException("Vote value cannot be null");
    }
    if (user.getIsBanned() || forum.getBannedUsers().contains(user)) {
      throw new DomainException("Banned users can't vote");
    }
    votes.put(user.getId(), voteType);
  }

  public Map<Integer, Boolean> getVotes() {
    return Collections.unmodifiableMap(votes);
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted() throws DomainException {
    isDeleted = true;
    setTitle(author, "Deleted");
    setContent(author, "This post was deleted!");
    votes.clear();
    comments.clear();
  }

}
