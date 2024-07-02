package com.example.services.repository;

import com.example.services.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByServiceId(Long serviceId);
}
