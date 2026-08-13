package mindev188.board.hotarticle.service.eventhandler;

import mindev188.board.common.event.Event;
import mindev188.board.common.event.EventType;
import mindev188.board.common.event.payload.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import mindev188.board.hotarticle.repository.ArticleCreatedTimeRepository;
import mindev188.board.hotarticle.repository.HotArticleListRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final HotArticleListRepository hotArticleListRepository;
    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();
        articleCreatedTimeRepository.delete(payload.getArticleId());
        hotArticleListRepository.remove(payload.getArticleId(), payload.getCreatedAt());

    }

    @Override
    public boolean support(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getType();
    }

    @Override
    public Long findArticleId(Event<ArticleDeletedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
