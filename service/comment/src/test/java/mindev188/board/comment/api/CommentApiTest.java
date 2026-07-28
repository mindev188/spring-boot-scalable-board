package mindev188.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mindev188.board.comment.service.response.CommentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class CommentApiTest {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = createComment(new CommentCreateRequest(1L, "my comment1", null, 1L));
        CommentResponse response2 = createComment(new CommentCreateRequest(1L, "my comment2", response1.getCommentId(), 1L));
        CommentResponse response3 = createComment(new CommentCreateRequest(1L, "my comment3", response1.getCommentId(), 1L));

        System.out.println("commentId=%s".formatted(response1.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response2.getCommentId()));
        System.out.println("\tcommentId=%s".formatted(response3.getCommentId()));

    }

    @Test
    void read() {
        restClient.get()
                .uri("/v1/comments/{commentId}", 340380596252565504L)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void delete() {
        // commentId=340380596252565504
        //      commentId=340380597359861760
        //      commentId=340380597494079488

        restClient.delete()
                .uri("/v1/comments/{commentId}", 340380597494079488L)
                .retrieve()
                .toBodilessEntity();
    }

    CommentResponse createComment(CommentCreateRequest request) {
        return restClient.post()
                .uri("/v1/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long parentCommentId;
        private Long writerId;
    }
}
