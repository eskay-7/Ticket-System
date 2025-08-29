package io.eskay.ticket_system.mapper;

import io.eskay.ticket_system.dto.response.FaqDto;
import io.eskay.ticket_system.entity.Faq;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FaqDtoMapper implements Function<Faq, FaqDto> {
    @Override
    public FaqDto apply(Faq faq) {
        return new FaqDto(
                faq.getId(),
                faq.getQuestion(),
                faq.getAnswer(),
                faq.getCategory().getName()
        );
    }
}
