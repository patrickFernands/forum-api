package com.example.forum.forum_api.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.forum.forum_api.entities.*;
import com.example.forum.forum_api.exceptions.DomainException;

public class PlatformService {

    private HashMap<String, User> users;
    private HashMap<String, Forum> forums;
    private User currentUser;
    private Forum currentForum;
    private Post currentPost;

    public PlatformService() {
        users = new HashMap<>();
        forums = new HashMap<>();
        currentUser = null;
        currentForum = null;
        currentPost = null;
        User root = new User("root", "root123");
        root.setIsAdmin(true);
        users.put("root", root);
    }

    // cuidado ao implementar logica de banimento e remoção!, falta usuario poder
    // excluir conta.
    // colocar função pra editar post e comentarios.

    // authentication.
    public void signUp(String name, String password) throws DomainException {
        if (currentUser != null) {
            throw new DomainException("You must sign out to create a new Account");
        }
        if (users.containsKey(name)) {
            throw new DomainException("An user already has this name");
        }
        users.put(name, new User(name, password));
        currentUser = users.get(name);
    }

    public void signIn(String name, String password) throws DomainException {

        if (users.isEmpty()) {
            throw new DomainException("You must create an account first");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User does not exist!");
        }
        if (!user.getPassword().equals(password)) {
            throw new DomainException("Invalid credentials");
        }
        if (currentUser != null) {
            throw new DomainException("Already signed in!");
        }
        currentUser = users.get(name);
    }

    public void signOut() throws DomainException {
        if (currentUser == null) {
            throw new DomainException("Already logged off!");
        }
        currentUser = null;
        currentForum = null;
        currentPost = null;
    }
    //

    // forums
    public void createForum(String name) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be signed in!");
        }
        if (forums.containsKey(name)) {
            throw new DomainException("A forum with this name already exists!");
        }
        Forum newForum = new Forum(name, currentUser);
        forums.put(name, newForum);
        currentForum = forums.get(name);
    }

    public void deleteForum(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be signed in!");
        }

        Forum forum = forums.get(name);

        if (forum == null) {
            throw new DomainException("Forum not found");
        }

        if (!(currentUser.equals(forum.getCreator()) || currentUser.getIsAdmin())) {
            throw new DomainException("You don't have the permission to delete this forum.");
        }
        currentForum = null;
        forums.get(name).setIsDeleted();
    }

    public void changeForumName(String name) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to edit the name");
        }
        if (currentForum == null) {
            throw new DomainException("You must open the forum to edit the name!");
        }
        if (forums.containsKey(name)) {
            throw new DomainException("This name is not available");
        }
        String oldName = currentForum.getName();
        currentForum.setName(currentUser, name);
        forums.put(name, currentForum);
        forums.remove(oldName);
    }
    //

    // posts
    public void addPost(String title, String content) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to add post");
        }
        if (currentForum == null) {
            throw new DomainException("You must acess a forum to post!");
        }
        currentForum.addPost(currentUser, new Post(currentUser, title, content));
    }

    public void approvePost(Integer postId, Boolean isApproved) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to approve posts");
        }
        if (currentForum == null) {
            throw new DomainException("You must acess a forum to approve posts!");
        }
        currentForum.approvePost(currentUser, postId, isApproved);
    }

    public void deletePost(Integer postId) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to approve posts");
        }
        if (currentForum == null) {
            throw new DomainException("You must acess a forum to delete posts!");
        }
        currentForum.removePost(currentUser, postId);
    }

    public void editPostTitle(String title) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to edit posts");
        }
        if (currentPost == null) {
            throw new DomainException("You must acess the post you want to edit");
        }
        currentPost.setTitle(currentUser, title);
    }

    public void editPost(String content) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to edit posts");
        }
        if (currentPost == null) {
            throw new DomainException("You must acess the post you want to edit");
        }
        currentPost.setContent(currentUser, content);
    }

    //

    // comments
    public void addComment(String content) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to add a comment");
        }
        if (currentForum == null) {
            throw new DomainException("You must acess a post in a forum to add a comment");
        }
        if (currentPost == null) {
            throw new DomainException("You must open a post to add a comment");
        }
        currentPost.addComment(new Comment(currentUser, content), currentForum);
    }

    public void deleteComment(Integer commentId) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to remove a comment");
        }
        if (currentForum == null) {
            throw new DomainException("You must acess a post in a forum to remove a comment");
        }
        if (currentPost == null) {
            throw new DomainException("You must open a post to remove a comment");
        }
        currentPost.removeComment(currentUser, commentId, currentForum);
    }

    public void addNestedComment(Integer upperCommentId, String content) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to add a comment");
        }
        if (currentForum == null) {
            throw new DomainException("You must acess a post in a forum to add a comment");
        }
        if (currentPost == null) {
            throw new DomainException("You must open a post to add a comment");
        }
        currentPost.addNestedComment(upperCommentId, new Comment(currentUser, content), currentForum);
    }

    public void editComment(Integer commentId, String text) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to edit comments");
        }
        if (currentPost == null) {
            throw new DomainException("You must acess the post where the comment is to edit");
        }

        Comment comment = currentPost.getComments().get(commentId);

        if (comment == null) {
            throw new DomainException("Comment not found");
        }

        comment.setText(currentUser, text);
    }
    //

    // navigation
    public List<String> findForums(String forumName) throws DomainException {

        if (forumName == null || forumName.equals("")) {
            throw new DomainException("You must digit the name of a forum");
        }

        List<String> possibleForums = new ArrayList<>();
        forumName = forumName.toLowerCase();

        for (Forum forum : forums.values()) {
            if (forum.getName().toLowerCase().contains(forumName)) {
                possibleForums.add(forum.getName());
            }
        }

        return Collections.unmodifiableList(possibleForums);
    }

    public List<String> findUser(String userName) throws DomainException {

        if (userName == null || userName.equals("")) {
            throw new DomainException("You must digit the name of an user");
        }

        List<String> possibleUsers = new ArrayList<>();
        userName = userName.toLowerCase();

        for (User user : users.values()) {
            if (user.getName().toLowerCase().contains(userName)) {
                possibleUsers.add(user.getName());
            }
        }

        return Collections.unmodifiableList(possibleUsers);

    }

    public Map<Integer, Post> forumSearch(String search) throws DomainException {

        if (currentForum == null) {
            throw new DomainException("You must open a forum to search!");
        }
        if (search == null || search.equals("")) {
            throw new DomainException("You must type something to search");
        }

        Map<Integer, Post> foundPosts = new HashMap<>();
        search = search.toLowerCase();

        for (Post post : currentForum.getApprovedPosts().values()) {
            if (post.getTitle().toLowerCase().contains(search) || post.getContent().toLowerCase().contains(search)) {
                foundPosts.put(post.getId(), post);
            }
        }

        return Collections.unmodifiableMap(foundPosts);

    }
    //

    // banning
    public void banUserFromForum(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be logged to add new admins");
        }

        if (currentForum == null) {
            throw new DomainException("You must access the forum page");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User not found!");
        }
        if (user.getIsAdmin()) {
            throw new DomainException("Can't ban global moderators");
        }
        currentForum.banUser(currentUser, user);
    }

    public void unbanUserFromForum(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be logged to unban users");
        }

        if (currentForum == null) {
            throw new DomainException("You must access the forum page");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User not found!");
        }
        if (user.getIsAdmin()) {
            throw new DomainException("Can't ban global moderators");
        }
        currentForum.unbanUser(currentUser, user);
    }

    public void banUserFromAll(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be logged to ban users");
        }

        if (!currentUser.getIsAdmin()) {
            throw new DomainException("You aren't allowed to ban users!");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User doesn't exist");
        }

        user.setIsBanned(true);

    }

    public void unbanUserFromAll(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be logged to unban users");
        }

        if (!currentUser.getIsAdmin()) {
            throw new DomainException("You aren't allowed to unban users!");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User doesn't exist");
        }

        user.setIsBanned(false);
    }
    //

    // votes
    public void voteInPost(Boolean isUpvote) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to vote");
        }
        if (currentForum == null) {
            throw new DomainException("You must acess a post in a forum to vote");
        }
        if (currentPost == null) {
            throw new DomainException("You must open a post to vote");
        }
        currentPost.addVote(currentUser, isUpvote, currentForum);
    }

    public void voteInComment(Integer commentId, Boolean isUpvote) throws DomainException {
        if (currentUser == null) {
            throw new DomainException("You must be logged to vote");
        }
        if (currentForum == null) {
            throw new DomainException("You must acess a post in a forum to vote");
        }
        if (currentPost == null) {
            throw new DomainException("You must open a post to vote");
        }

        currentPost.voteInComment(currentUser, commentId, isUpvote, currentForum);

    }
    //

    // Permissions
    public void removeForumAdmin(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be logged to remove admins");
        }

        if (currentForum == null) {
            throw new DomainException("You must access the forum page");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User not found!");
        }
        currentForum.removeAdmin(currentUser, user);
    }

    public void addForumAdmin(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be logged to add new admins");
        }

        if (currentForum == null) {
            throw new DomainException("You must access the forum page");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User not found!");
        }
        if (user.getIsBanned() || currentForum.getBannedUsers().contains(user)) {
            throw new DomainException("Banned users can't be forum admins");
        }
        currentForum.addAdmin(currentUser, user);
    }

    public void addAdmin(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be logged to add new admins");
        }

        if (!currentUser.equals(users.get("root"))) {
            throw new DomainException("You aren't root!");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User doesn't exist");
        }
        if (user.getIsBanned()) {
            throw new DomainException("Can't give admin to banned users");
        }
        user.setIsAdmin(true);
    }

    public void removeAdmin(String name) throws DomainException {

        if (currentUser == null) {
            throw new DomainException("You must be logged to add new admins");
        }

        if (!currentUser.equals(users.get("root"))) {
            throw new DomainException("You aren't root!");
        }

        User user = users.get(name);

        if (user == null) {
            throw new DomainException("User doesn't exist");
        }
        user.setIsAdmin(false);
    }
    //

}
