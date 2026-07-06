package com.example.forum.forum_api.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tb_vote")
public class Vote {

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User voter;

    @Column(name = "is_upvote", nullable = false)
    private Boolean isUpvote;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    public Vote(){

    }

    public Vote(User voter, Boolean isUpvote, Comment comment){
        this.voter = voter;
        this.isUpvote = isUpvote;
        this.comment = comment;
    }

     public User getVoter() {
        return voter;
    }

    public Boolean getIsUpvote() {
        return isUpvote;
    }

    public Long getId() {
        return id;
    }


}
