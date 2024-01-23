package br.com.fullcycle.hexagonal.application.domain.customer;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreateCustomer() {
        // given
        final var expectedCpf = "123.456.789-01";
        final var expectedName = "Vini";
        final var expectedEmail = "vini@vini.com";

        // when
        final var actualCustomer = Customer.newCustomer(expectedName, expectedCpf, expectedEmail);

        // then
        Assertions.assertNotNull(actualCustomer.customerId());
        Assertions.assertEquals(expectedCpf, actualCustomer.cpf().value());
        Assertions.assertEquals(expectedEmail, actualCustomer.email().value());
        Assertions.assertEquals(expectedName, actualCustomer.name().value());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com CPF inválido")
    public void testCreateCustomerWithInvalidCpf() {
        // given
        final var expectedError = "Invalid cpf";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> Customer.newCustomer("Vini", "112345678901", "vini@vini.com"));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar com nome inválido")
    public void testCreateCustomerWithInvalidName() {
        // given
        final var expectedError = "Invalid name";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> Customer.newCustomer(null, "123.456.789-01", "vini@vini.com"));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar com email inválido")
    public void testCreateCustomerWithInvalidEmail() {
        // given
        final var expectedError = "Invalid email";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> Customer.newCustomer("Vini", "123.456.789-01", "vini@"));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}
