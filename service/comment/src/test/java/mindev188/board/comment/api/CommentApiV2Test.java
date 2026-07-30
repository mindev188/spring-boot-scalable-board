package mindev188.board.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mindev188.board.comment.service.response.CommentPageResponse;
import mindev188.board.comment.service.response.CommentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiV2Test {
    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2(1L, "my comment1", response1.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2(1L, "my comment1", response2.getPath(), 1L));

        System.out.println("response1.getCommentId() = " + response1.getCommentId());
        System.out.println("response1.getPath() = " + response1.getPath());
        System.out.println("\tresponse2.getCommentId() = " + response2.getCommentId());
        System.out.println("\tresponse2.getPath() = " + response2.getPath());
        System.out.println("\t\tresponse3.getCommentId() = " + response3.getCommentId());
        System.out.println("\t\tresponse3.getPath() = " + response3.getPath());

        /**
         * response1.getCommentId() = 341036470532059136
         * response1.getPath() = 00002
         * 	response2.getCommentId() = 341036471102484480
         * 	response2.getPath() = 0000200000
         * 		response3.getCommentId() = 341036471232507904
         * 		response3.getPath() = 000020000000000
         */
    }

    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                .uri("/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }

    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v2/comments/{commentId}", 341036471232507904L)
                .retrieve()
                .body(CommentResponse.class);
        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("/v2/comments/{commentId}", 341036471232507904L)
                .retrieve();
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }
}
