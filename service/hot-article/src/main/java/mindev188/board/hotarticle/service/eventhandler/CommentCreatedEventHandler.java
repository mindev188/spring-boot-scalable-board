package mindev188.board.hotarticle.service.eventhandler;

import mindev188.board.common.event.Event;
import mindev188.board.common.event.EventType;
import mindev188.board.common.event.payload.CommentCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import mindev188.board.hotarticle.repository.ArticleCommentCountRepository;
import mindev188.board.hotarticle.utils.TimeCalculatorUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload> {
    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Override
    public void handle(Event<CommentCreatedEventPayload> event) {
        CommentCreatedEventPayload payload = event.getPayload();
        articleCommentCountRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getArticleCommentCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean support(Event<CommentCreatedEventPayload> event) {
        return EventType.COMMENT_CREATED.equals(event.getType());
    }

    @Override
    public Long findArticleId(Event<CommentCreatedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
