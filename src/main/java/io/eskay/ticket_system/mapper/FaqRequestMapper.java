package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.request.CreateFaqRequest;
import io.eskay.ticket_system.entity.Faq;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FaqRequestMapper implements Function<CreateFaqRequest, Faq> {
    @Override
    public Faq apply(CreateFaqRequest request) {
        return Faq.builder()
                .question(request.question())
                .answer(request.answer())
                .build();
    }
}
