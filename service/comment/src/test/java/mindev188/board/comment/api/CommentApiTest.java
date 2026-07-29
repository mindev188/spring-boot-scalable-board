package mindev188.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mindev188.board.comment.service.response.CommentPageResponse;
import mindev188.board.comment.service.response.CommentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

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

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v1/comments?articleId=1&page=1&pageSize=10")
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() " + comment.getCommentId());
        }

        /**
         comment.getCommentId() 340379445539557376
         comment.getCommentId() 340379556125978624
         comment.getCommentId() 340379830607364096
         comment.getCommentId() 340384923206713344
         comment.getCommentId() 340384923387068420
         comment.getCommentId() 340384923206713345
         comment.getCommentId() 340384923387068425
         comment.getCommentId() 340384923206713346
         comment.getCommentId() 340384923387068419
         comment.getCommentId() 340384923206713347
         */
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> response1 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        for (CommentResponse comment : response1) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() " + comment.getCommentId());
        }

        Long lastParentCommentId = response1.getLast().getParentCommentId();
        Long lastCommentId = response1.getLast().getCommentId();

        List<CommentResponse> response2 = restClient.get()
                .uri("/v1/comments/infinite-scroll?articleId=1&pageSize=5&lastParentCommentId=%s&lastCommentId=%s".formatted(lastParentCommentId, lastCommentId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        for (CommentResponse comment : response2) {
            if (!comment.getCommentId().equals(comment.getParentCommentId())) {
                System.out.print("\t");
            }
            System.out.println("comment.getCommentId() " + comment.getCommentId());
        }
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
