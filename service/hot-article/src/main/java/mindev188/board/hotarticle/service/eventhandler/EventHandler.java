package mindev188.board.hotarticle.service.eventhandler;

import kuke.board.common.event.Event;
import kuke.board.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean support(Event<T> event);
    Long findArticleId(Event<T> event);
}
