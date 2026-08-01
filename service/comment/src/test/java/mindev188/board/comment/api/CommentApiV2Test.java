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
                .retrieve()
                .toBodilessEntity();
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v2/comments?articleId=1&pageSize=10&page=50000")
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse commentResponse : response.getComments()) {
            System.out.println("comment.getCommentId() = " + commentResponse.getCommentId());
        }

        /**
         * comment.getCommentId() = 341035796058615808
         * comment.getCommentId() = 341035798566809600
         * comment.getCommentId() = 341035798768136192
         * comment.getCommentId() = 341035974350090240
         * comment.getCommentId() = 341035974832435200
         * comment.getCommentId() = 341035975004401664
         * comment.getCommentId() = 341036470532059136
         * comment.getCommentId() = 341036471102484480
         * comment.getCommentId() = 341036471232507904
         * comment.getCommentId() = 341041024971657219
         */
    }

    @Test
    public void readAllInfiniteScroll() {
        List<CommentResponse> response1 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {});

        System.out.println("firstPage");
        for (CommentResponse commentResponse : response1) {
            System.out.println("comment.getCommentId() = " + commentResponse.getCommentId());
        }

        String lastPath = response1.getLast().getPath();
         List<CommentResponse> response2 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath=%s".formatted(lastPath))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {});

        System.out.println("SecondPage");
        for (CommentResponse commentResponse : response1) {
            System.out.println("comment.getCommentId() = " + commentResponse.getCommentId());
        }
    }

    @Test
    void countTest() {
        CommentResponse response = create(new CommentCreateRequestV2(2L, "my comment1", null, 1L));

        Long count1 = restClient.get()
                .uri("/v2/comments/articles/{articleId}/count", 2L)
                .retrieve()
                .body(Long.class);
        System.out.println("count = " + count1);

        restClient.delete()
                .uri("/v2/comments/{commentId}", response.getCommentId())
                .retrieve()
                .toBodilessEntity();

        Long count2 = restClient.get()
                .uri("/v2/comments/articles/{articleId}/count", 2L)
                .retrieve()
                .body(Long.class);
        System.out.println("count = " + count2);
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
