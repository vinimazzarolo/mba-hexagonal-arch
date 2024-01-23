package br.com.fullcycle.hexagonal.application.domain.partner;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PartnerTest {

    @Test
    @DisplayName("Deve criar um partner")
    public void testCreateCustomer() {
        // given
        final var expectedCnpj = "41.536.538/0001-00";
        final var expectedName = "John Doe";
        final var expectedEmail = "john.doe@gmail.com";

        // when
        final var actualPartner = Partner.newPartner(expectedName, expectedCnpj, expectedEmail);

        // then
        Assertions.assertNotNull(actualPartner.partnerId());
        Assertions.assertEquals(expectedCnpj, actualPartner.cnpj().value());
        Assertions.assertEquals(expectedEmail, actualPartner.email().value());
        Assertions.assertEquals(expectedName, actualPartner.name().value());
    }

    @Test
    @DisplayName("Não deve instanciar um partner com CNPJ inválido")
    public void testCreateCustomerWithInvalidCpf() {
        // given
        final var expectedError = "Invalid cnpj";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> Partner.newPartner("John Doe", "41.536.5380001-00", "john.doe@gmail.com"));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar com nome inválido")
    public void testCreateCustomerWithInvalidName() {
        // given
        final var expectedError = "Invalid name";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> Partner.newPartner(null, "41.536.538/0001-00", "john.doe@gmail.com"));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar com email inválido")
    public void testCreateCustomerWithInvalidEmail() {
        // given
        final var expectedError = "Invalid email";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> Partner.newPartner("John Doe", "41.536.538/0001-00", "john"));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}
