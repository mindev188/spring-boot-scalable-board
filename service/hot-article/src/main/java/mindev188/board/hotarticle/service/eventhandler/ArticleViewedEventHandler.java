package mindev188.board.hotarticle.service.eventhandler;

import kuke.board.common.event.Event;
import kuke.board.common.event.EventType;
import kuke.board.common.event.payload.ArticleViewdEventPayload;
import lombok.RequiredArgsConstructor;
import mindev188.board.hotarticle.repository.ArticleViewCountRepository;
import mindev188.board.hotarticle.utils.TimeCalculatorUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleViewedEventHandler implements EventHandler<ArticleViewdEventPayload> {
    private final ArticleViewCountRepository articleViewCountRepository;

    @Override
    public void handle(Event<ArticleViewdEventPayload> event) {
        ArticleViewdEventPayload payload = event.getPayload();
        articleViewCountRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getArticleViewcount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean support(Event<ArticleViewdEventPayload> event) {
        return EventType.ARTICLE_VIEWED.equals(event.getType());
    }

    @Override
    public Long findArticleId(Event<ArticleViewdEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
