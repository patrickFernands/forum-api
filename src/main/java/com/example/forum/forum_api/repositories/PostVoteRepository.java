package com.example.forum.forum_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.forum_api.entities.PostVote;

public interface PostVoteRepository extends JpaRepository<PostVote, Long> {

}
