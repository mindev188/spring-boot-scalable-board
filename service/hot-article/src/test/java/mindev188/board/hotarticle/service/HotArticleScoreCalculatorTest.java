package mindev188.board.hotarticle.service;

import mindev188.board.hotarticle.repository.ArticleCommentCountRepository;
import mindev188.board.hotarticle.repository.ArticleLikeCountRepository;
import mindev188.board.hotarticle.repository.ArticleViewCountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreCalculatorTest {
    @InjectMocks
    HotArticleScoreCalculator hotArticleScoreCalculator;
    @Mock
    ArticleLikeCountRepository articleLikeCountRepository;
    @Mock
    ArticleViewCountRepository articleViewCountRepository;
    @Mock
    ArticleCommentCountRepository articleCommentCountRepository;

    @Test
    void calculateTest() {
        // given
        Long articleId = 1L;
        Long likeCount = RandomGenerator.getDefault().nextLong(100);
        Long viewCount = RandomGenerator.getDefault().nextLong(100);
        Long commentCount = RandomGenerator.getDefault().nextLong(100);
        given(articleLikeCountRepository.read(articleId)).willReturn(likeCount);
        given(articleViewCountRepository.read(articleId)).willReturn(viewCount);
        given(articleCommentCountRepository.read(articleId)).willReturn(commentCount);

        // when
        long score = hotArticleScoreCalculator.calculate(articleId);

        // then
        assertThat(score).isEqualTo(3 * likeCount + 2 * commentCount + 1 * viewCount);
    }
}