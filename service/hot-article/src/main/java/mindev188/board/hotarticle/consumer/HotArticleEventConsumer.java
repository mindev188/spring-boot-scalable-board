package mindev188.board.hotarticle.consumer;

import kuke.board.common.event.Event;
import kuke.board.common.event.EventPayload;
import kuke.board.common.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mindev188.board.hotarticle.service.HotArticleService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {
    private final HotArticleService hotArticleService;

    @KafkaListener(topics = {
            EventType.Topic.SCALABLE_BOARD_ARTICLE,
            EventType.Topic.SCALABLE_BOARD_COMMENT,
            EventType.Topic.SCALABLE_BOARD_LIKE,
            EventType.Topic.SCALABLE_BOARD_VIEW
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[HotArticleEventConsumer.listen] received message = {}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            hotArticleService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
