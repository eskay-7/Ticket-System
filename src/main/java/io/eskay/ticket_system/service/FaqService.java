package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateFaqRequest;
import io.eskay.ticket_system.dto.request.UpdateFaqRequest;
import io.eskay.ticket_system.dto.response.FaqDto;

import java.util.List;

public interface FaqService {
    FaqDto createFaq(CreateFaqRequest request);

    List<FaqDto> getTeamFaqs();

    void deleteFaq(Long faqId);

    FaqDto updateFaq(Long faqId, UpdateFaqRequest request);

    List<FaqDto> getFaqs(int category);
}
