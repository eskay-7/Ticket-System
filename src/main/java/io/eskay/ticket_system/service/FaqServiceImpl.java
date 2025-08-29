package io.eskay.ticket_system.service;

import io.eskay.ticket_system.dto.request.CreateFaqRequest;
import io.eskay.ticket_system.dto.request.UpdateFaqRequest;
import io.eskay.ticket_system.dto.response.FaqDto;
import io.eskay.ticket_system.exception.ResourceNotFoundException;
import io.eskay.ticket_system.mapper.FaqDtoMapper;
import io.eskay.ticket_system.mapper.FaqRequestMapper;
import io.eskay.ticket_system.repository.CategoryRepository;
import io.eskay.ticket_system.repository.FaqRepository;
import io.eskay.ticket_system.repository.UserRepository;
import io.eskay.ticket_system.util.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;
    private final CategoryRepository categoryRepository;
    private final FaqRequestMapper faqRequestMapper;
    private final FaqDtoMapper faqDtoMapper;
    private final UserRepository userRepository;

    public FaqServiceImpl(
            FaqRepository faqRepository,
            CategoryRepository categoryRepository,
            FaqRequestMapper faqRequestMapper,
            FaqDtoMapper faqDtoMapper,
            UserRepository userRepository
    ) {
        this.faqRepository = faqRepository;
        this.categoryRepository = categoryRepository;
        this.faqRequestMapper = faqRequestMapper;
        this.faqDtoMapper = faqDtoMapper;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public FaqDto createFaq(CreateFaqRequest request) {
        var category = categoryRepository.findById(request.category_id())
                .orElseThrow(() -> new IllegalArgumentException("Error, no category found with id '%d'"
                        .formatted(request.category_id())));

        var faq = faqRequestMapper.apply(request);
        faq.setCategory(category);
        return faqDtoMapper.apply(faqRepository.save(faq));
    }

    @Override
    public List<FaqDto> getTeamFaqs() {
        var user = userRepository.findByEmailWithTeam(AuthenticatedUser.get().getEmail()).get();

        return faqRepository.findAllByTeam(user.getTeam())
                .stream()
                .map(faqDtoMapper)
                .toList();
    }

    @Override
    public void deleteFaq(Long faqId) {
        var user = userRepository.findByEmailWithTeam(AuthenticatedUser.get().getEmail()).get();

        faqRepository.findByIdAndTeam(faqId, user.getTeam())
                .orElseThrow(() -> new ResourceNotFoundException("Error, faq with id '%d' does not belong to this team"
                        .formatted(faqId)));
        faqRepository.deleteById(faqId);
    }

    @Override
    @Transactional
    public FaqDto updateFaq(Long faqId, UpdateFaqRequest request) {
        var user = userRepository.findByEmailWithTeam(AuthenticatedUser.get().getEmail()).get();

        var foundFaq = faqRepository.findByIdAndTeam(faqId, user.getTeam())
                .orElseThrow(() -> new ResourceNotFoundException("Error, faq with id '%d' does not belong to this team"
                        .formatted(faqId)));

        foundFaq.setQuestion(request.question());
        foundFaq.setAnswer(request.answer());

        return faqDtoMapper.apply(faqRepository.save(foundFaq));
    }

    @Override
    public List<FaqDto> getFaqs(int categoryId) {
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Error, no category found with id '%d'"
                        .formatted(categoryId)));

        return faqRepository.findAllByCategory(category)
                .stream()
                .map(faqDtoMapper)
                .toList();
    }
}
