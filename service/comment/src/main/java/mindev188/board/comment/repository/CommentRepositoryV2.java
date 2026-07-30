package mindev188.board.comment.repository;

import mindev188.board.comment.entity.CommentV2;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface CommentRepositoryV2 extends CrudRepository<CommentV2, Long> {

    @Query("select c from CommentV2 c where c.commentPath.path = :path")
    Optional<CommentV2> findByPath(@Param("path") String path);

    @Query(
            value = "select path from comment_v2 " +
                    "where article_id = :articleId and path > :pathPrefix and path like :pathPrefix% " +
                    "order by path desc limit 1",
            nativeQuery = true
    )
    Optional<String> findDescendantsTopPath(
            @Param("articleId") Long articleId,
            @Param("pathPrefix") String pathPrefix
    );
}
