package mindev188.board.comment.controller;

import lombok.RequiredArgsConstructor;
import mindev188.board.comment.entity.Comment;
import mindev188.board.comment.service.CommentService;
import mindev188.board.comment.service.request.CommentCreateRequest;
import mindev188.board.comment.service.response.CommentResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/v1/comments/{commentId}")
    public CommentResponse read(@PathVariable("commentId") Long commentId) {
        return commentService.read(commentId);
    }

    @PostMapping("/v1/comments")
    public CommentResponse create(@RequestBody CommentCreateRequest request) {
        return commentService.create(request);
    }

    @DeleteMapping("/v1/comments/{commentId}")
    public void delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
    }

}
