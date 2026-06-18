package br.com.dogvision.dogfeeding.service.imp;

import br.com.dogvision.dogfeeding.dto.create.CreateRationRequest;
import br.com.dogvision.dogfeeding.dto.mapper.RationMapperImpl;
import br.com.dogvision.dogfeeding.infra.exception.InvalidRationStateException;
import br.com.dogvision.dogfeeding.model.Ration;
import br.com.dogvision.dogfeeding.model.RationStockStatus;
import br.com.dogvision.dogfeeding.model.RationType;
import br.com.dogvision.dogfeeding.repository.FeedingItemRepository;
import br.com.dogvision.dogfeeding.repository.RationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RationServiceImpTest {

    @Mock
    private RationRepository rationRepository;

    @Mock
    private FeedingItemRepository feedingItemRepository;

    private RationServiceImp service;

    @BeforeEach
    void setUp() {
        service = new RationServiceImp(rationRepository, feedingItemRepository, new RationMapperImpl());
    }

    @Test
    void shouldRejectCurrentStockGreaterThanTotal() {
        CreateRationRequest request = new CreateRationRequest(
                "Premium",
                RationType.NORMAL,
                10.0,
                12.0,
                LocalDate.now()
        );

        assertThatThrownBy(() -> service.save(request, null))
                .isInstanceOf(InvalidRationStateException.class);
    }

    @Test
    void shouldReturnOnlyMatchingStockStatus() {
        Ration healthy = ration("Healthy", 10.0, 8.0);
        Ration low = ration("Low", 10.0, 1.0);

        when(rationRepository.findAll()).thenReturn(List.of(healthy, low));

        List<?> result = service.search(null, RationStockStatus.LOW);

        assertThat(result).hasSize(1);
        assertThat(service.search(null, RationStockStatus.LOW).getFirst().name()).isEqualTo("Low");
    }

    @Test
    void shouldPersistValidRation() {
        when(rationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.save(new CreateRationRequest(
                "Premium",
                RationType.NORMAL,
                10.0,
                5.0,
                LocalDate.now()
        ), null);

        assertThat(response.stockStatus()).isEqualTo(RationStockStatus.HEALTHY);
    }

    @Test
    void shouldReturnOnlyStockAlerts() {
        Ration healthy = ration("Healthy", 10.0, 8.0);
        Ration low = ration("Low", 10.0, 1.0);
        Ration empty = ration("Empty", 10.0, 0.0);

        when(rationRepository.findAll()).thenReturn(List.of(healthy, low, empty));

        var alerts = service.alerts();

        assertThat(alerts).hasSize(2);
        assertThat(alerts).extracting("rationName").containsExactly("Empty", "Low");
    }

    private Ration ration(String name, double total, double current) {
        Ration ration = new Ration();
        ration.setName(name);
        ration.setRationType(RationType.NORMAL);
        ration.setTotalRationQuantity(total);
        ration.setCurrentRationQuantity(current);
        ration.setRegistrationDate(LocalDate.now().minusDays(1));
        return ration;
    }
}
