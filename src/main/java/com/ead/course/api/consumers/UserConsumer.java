package com.ead.course.api.consumers;

import com.ead.course.api.dtos.event.UserEventDTO;
import com.ead.course.domain.enums.ActionType;
import com.ead.course.domain.models.User;
import com.ead.course.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final UserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.userEventQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.userEventExchange}",
                    type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
    ))
    public void listenUserEvent(@Payload UserEventDTO userEventDTO) {
        User user = UserEventDTO.toUser(userEventDTO);

        switch (ActionType.valueOf(userEventDTO.getActionType())) {
            case CREATE:
                userService.save(user);
                break;
        }
    }
}
