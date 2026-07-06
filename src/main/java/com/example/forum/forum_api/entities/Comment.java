package com.example.forum.forum_api.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.forum.forum_api.exceptions.DomainException;

public class Comment {

  private static Integer idCount = 1;

  private Integer id;
  private String text;
  private User author;
  private List<Comment> commentNest;
  private Map<Integer, Boolean> votes;
  private Boolean isDeleted;

  public Comment() {
  }

  public Comment(User author, String text) throws DomainException {
    this.author = author;
    if (this.author == null) {
      throw new DomainException("A comment must have an author!");
    }
    id = idCount;
    idCount++;
    this.text = text;
    if (this.text == null || this.text.equals("")) {
      throw new DomainException("Empty comment!");
    }
    commentNest = new ArrayList<>();
    votes = new HashMap<>();
    isDeleted = false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
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

  public Integer getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public void setText(User user, String text) throws DomainException {
    if (!user.equals(author)) {
      throw new DomainException("You aren't allowed to edit this comment!");
    }
    if (text == null || text.equals("")) {
      throw new DomainException("Your comment must have a text!");
    }
    this.text = text;
  }

  public User getAuthor() {
    return author;
  }

  public void addNestedComment(Comment nestedComment) {
    commentNest.add(nestedComment);
  }

  public void removeNestedComment(Comment nestedComment) throws DomainException {
    nestedComment.setIsDeleted();
  }

  public List<Comment> getCommentNest() {
    return Collections.unmodifiableList(commentNest);
  }

  public void addVote(User user, Boolean vote, Forum forum) throws DomainException {
    if (vote == null) {
      throw new DomainException("Vote value cannot be null");
    }
    if (user.getIsBanned() || forum.getBannedUsers().contains(user)) {
      throw new DomainException("Banned users can't vote");
    }
    votes.put(user.getId(), vote);
  }

  public Map<Integer, Boolean> getVotes() {
    return Collections.unmodifiableMap(votes);
  }

  public int getTotalVotes() {

    int totalVotes = 0;

    for (Boolean vote : votes.values()) {
      if (vote) {
        totalVotes++;
      } else {
        totalVotes--;
      }
    }

    return totalVotes;

  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted() throws DomainException {
    this.setText(author, "Comment was deleted.");
    this.isDeleted = true;
  }

}
