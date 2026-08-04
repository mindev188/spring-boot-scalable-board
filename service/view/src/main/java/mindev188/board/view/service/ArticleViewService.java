package mindev188.board.view.service;

import lombok.RequiredArgsConstructor;
import mindev188.board.view.repository.ArticleViewCountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final ArticleViewCountBackupProcessor articleViewCountBackupProcessor;
    private final ArticleViewCountRepository articleViewCountRepository;
    private static final int BACK_UP_BACH_SIZE = 100;

    public Long increase(Long articleId, Long userId) {
        Long count = articleViewCountRepository.increase(articleId);
        if (count % BACK_UP_BACH_SIZE == 0) {
            articleViewCountBackupProcessor.backUp(articleId, count);
        }
        return count;
    }

    public Long count(Long articleId) {
        return articleViewCountRepository.read(articleId);
    }
}
