package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.application.InMemoryPartnerRepository;
import br.com.fullcycle.hexagonal.application.domain.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CreatePartnerUseCaseTest {

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {
        // given
        final var expectedCnpj = "41.536.538/0001-00";
        final var expectedName = "John Doe";
        final var expectedEmail = "john.doe@gmail.com";
        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedName, expectedEmail);

        final var partnerRepository = new InMemoryPartnerRepository();
        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var output = useCase.execute(createInput);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCnpj, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCNPJShouldFail() throws Exception {
        // given
        final var expectedCnpj = "41.536.538/0001-00";
        final var expectedName = "John Doe";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedError = "Partner already exists";
        final var aPartner = Partner.newPartner(expectedName, expectedCnpj, "john@gmail.com");
        final var partnerRepository = new InMemoryPartnerRepository();

        partnerRepository.create(aPartner);

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedName, expectedEmail);

        // when
        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        // given
        final var expectedCnpj = "41.536.538/0001-00";
        final var expectedName = "John Doe";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedError = "Partner already exists";
        final var aPartner = Partner.newPartner(expectedName, "41.536.538/0002-00", expectedEmail);
        final var partnerRepository = new InMemoryPartnerRepository();

        partnerRepository.create(aPartner);

        final var createInput = new CreatePartnerUseCase.Input(expectedCnpj, expectedName, expectedEmail);

        final var useCase = new CreatePartnerUseCase(partnerRepository);
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
