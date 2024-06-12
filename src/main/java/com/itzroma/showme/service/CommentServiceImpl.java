package com.itzroma.showme.service;

import com.itzroma.showme.domain.entity.Comment;
import com.itzroma.showme.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
