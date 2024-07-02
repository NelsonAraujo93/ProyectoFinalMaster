package com.example.services.service;

import com.example.services.model.Comment;
import com.example.services.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(String id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> getCommentsByServiceId(Long serviceId) {
        return commentRepository.findByServiceId(serviceId);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
