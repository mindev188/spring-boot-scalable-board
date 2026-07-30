package mindev188.board.like.controller;

import lombok.RequiredArgsConstructor;
import mindev188.board.like.service.ArticleLikeService;
import mindev188.board.like.service.response.ArticleLikeResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;

    @GetMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
    public ArticleLikeResponse read(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        return articleLikeService.read(articleId, userId);
    }

    @PostMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
    public void like (
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        System.out.println("==================");
        System.out.println("articleId: %s, userId: %s".formatted(articleId, userId));
        articleLikeService.like(articleId, userId);
    }

    @DeleteMapping("/v1/article-likes/articles/{articleId}/users/{userId}")
    public void unlike (
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        articleLikeService.unlike(articleId, userId);
    }
}
