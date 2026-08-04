package mindev188.board.view.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mindev188.board.view.entity.ArticleViewCount;
import mindev188.board.view.repository.ArticleViewCountBackupRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackupProcessor {
    private final ArticleViewCountBackupRepository articleViewCountBackupRepository;

    @Transactional
    public void backUp(Long articleId, Long viewCount) {
        int result = articleViewCountBackupRepository.updateViewCount(articleId, viewCount);
        if (result == 0) {
            articleViewCountBackupRepository.findById(articleId)
                    .ifPresentOrElse(ignored -> { },
                            () -> articleViewCountBackupRepository.save(
                                    ArticleViewCount.init(articleId, viewCount)
                            ));
        }
    }
}
