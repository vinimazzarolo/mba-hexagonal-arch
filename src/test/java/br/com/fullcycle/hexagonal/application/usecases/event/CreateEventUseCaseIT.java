package br.com.fullcycle.hexagonal.application.usecases.event;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateEventUseCaseIT extends IntegrationTest {

    @Autowired
    private CreateEventUseCase useCase;

    @Autowired
    private PartnerJpaRepository partnerJpaRepository;

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @BeforeEach
    void tearDown() {
        partnerJpaRepository.deleteAll();
        eventJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreate() throws Exception {
        // given
        final var partner = createPartner("41536538000100", "john.doe@gmail.com", "John Doe");
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = partner.getId().toString();
        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);

        // when
        final var output = useCase.execute(createInput);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDate, output.date());
        Assertions.assertEquals(expectedTotalSpots, output.totalSpots());
        Assertions.assertEquals(expectedPartnerId, output.partnerId());
    }

    @Test
    @DisplayName("Não deve criar um evento quando o Partner não for encontrado")
    public void testCreateEventWhenPartnerDoesntExists_ShouldThrowError() throws Exception {
        // given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 10;
        final var expectedPartnerId = PartnerId.unique().value();
        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId, expectedTotalSpots);
        final var expectedError = "Partner not found";

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private PartnerEntity createPartner(final String cnpj, final String name, final String email) {
        final var partner = new PartnerEntity();
        partner.setCnpj(cnpj);
        partner.setName(name);
        partner.setEmail(email);
        return partnerJpaRepository.save(partner);
    }
}