package br.com.dogvision.dogfeeding.service.imp;

import br.com.dogvision.dogfeeding.dto.create.CreateFeedingItemRequest;
import br.com.dogvision.dogfeeding.dto.create.CreateFeedingRequest;
import br.com.dogvision.dogfeeding.dto.mapper.FeedingMapperImpl;
import br.com.dogvision.dogfeeding.infra.exception.InsufficientRationStockException;
import br.com.dogvision.dogfeeding.model.MealType;
import br.com.dogvision.dogfeeding.model.MeasurementUnit;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingItemRepository;
import br.com.dogvision.dogfeeding.repository.FeedingRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedingServiceImpTest {

    @Mock
    private FeedingRepository feedingRepository;

    @Mock
    private FeedingItemRepository feedingItemRepository;

    @Mock
    private RationRepository rationRepository;

    private FeedingServiceImp service;

    @BeforeEach
    void setUp() {
        service = new FeedingServiceImp(feedingRepository, feedingItemRepository, rationRepository, new FeedingMapperImpl());
    }

    @Test
    void shouldDiscountStockWhenSavingFeeding() {
        UUID rationId = UUID.randomUUID();
        Ration ration = ration(rationId, 10.0, 8.0);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(ration));
        when(feedingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CreateFeedingRequest request = new CreateFeedingRequest(
                UUID.randomUUID(),
                LocalDate.now(),
                LocalTime.of(8, 0),
                MealType.BREAKFAST,
                List.of(new CreateFeedingItemRequest(rationId, 1.5, MeasurementUnit.KILOGRAM)),
                "normal feeding",
                "ate well"
        );

        service.save(request, UUID.randomUUID());

        assertThat(ration.getCurrentRationQuantity()).isEqualTo(6.5);
        ArgumentCaptor<br.com.dogvision.dogfeeding.model.Feeding> captor = ArgumentCaptor.forClass(br.com.dogvision.dogfeeding.model.Feeding.class);
        verify(feedingRepository).save(captor.capture());
        assertThat(captor.getValue().getQuantity()).isEqualTo(1.5);
        assertThat(captor.getValue().getItems()).hasSize(1);
    }

    @Test
    void shouldRejectInsufficientStock() {
        UUID rationId = UUID.randomUUID();
        Ration ration = ration(rationId, 5.0, 0.5);

        when(rationRepository.findById(rationId)).thenReturn(Optional.of(ration));

        CreateFeedingRequest request = new CreateFeedingRequest(
                UUID.randomUUID(),
                LocalDate.now(),
                LocalTime.of(18, 0),
                MealType.DINNER,
                List.of(new CreateFeedingItemRequest(rationId, 1.0, MeasurementUnit.KILOGRAM)),
                null,
                null
        );

        assertThatThrownBy(() -> service.save(request, UUID.randomUUID()))
                .isInstanceOf(InsufficientRationStockException.class);
    }

    private Ration ration(UUID id, double total, double current) {
        Ration ration = new Ration();
        ration.setId(id);
        ration.setName("Premium");
        ration.setRationType(RationType.NORMAL);
        ration.setTotalRationQuantity(total);
        ration.setCurrentRationQuantity(current);
        ration.setRegistrationDate(LocalDate.now().minusDays(2));
        return ration;
    }
}
