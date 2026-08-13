package mindev188.board.common.event.payload;

import mindev188.board.common.event.EventPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleViewdEventPayload implements EventPayload {
    private Long articleId;
    private Long articleViewcount;
}
