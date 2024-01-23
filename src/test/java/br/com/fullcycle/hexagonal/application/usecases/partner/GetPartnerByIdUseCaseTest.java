package br.com.fullcycle.hexagonal.application.usecases.partner;


import br.com.fullcycle.hexagonal.application.repository.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class GetPartnerByIdUseCaseTest {
    @Test
    @DisplayName("Deve obter um parceiro por id")
    public void testGetById() {
        // given
        final var expectedCnpj = "41.536.538/0001-00";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var aPartner = Partner.newPartner(expectedName, expectedCnpj, expectedEmail);

        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);

        final var expectedId = aPartner.partnerId().value();
        final var input = new GetPartnerByIdUseCase.Input(expectedId);

        // when
        final var useCase = new GetPartnerByIdUseCase(partnerRepository);
        final var output = useCase.execute(input).get();

        // then
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedCnpj, output.cnpj());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedEmail, output.email());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um parceiro não existente por id")
    public void testGetByIdWithInvalidId() {
        // given
        final var expectedId = UUID.randomUUID().toString();
        final var input = new GetPartnerByIdUseCase.Input(expectedId);


        // when
        final var partnerRepository = new InMemoryPartnerRepository();
        final var useCase = new GetPartnerByIdUseCase(partnerRepository);
        final var output = useCase.execute(input);

        // then
        Assertions.assertTrue(output.isEmpty());
    }
}