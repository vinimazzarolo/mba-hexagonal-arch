package br.com.fullcycle.hexagonal.application.person;

import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CpfTest {

    @Test
    @DisplayName("Deve instanciar um CPF")
    public void testeCreateCpf() {
        // given
        final var expectedCpf = "123.456.789-01";

        // when
        final var actualCpf = new Cpf(expectedCpf);

        // then
        Assertions.assertEquals(expectedCpf, actualCpf.value());
    }

    @Test
    @DisplayName("Não deve instanciar um CPF inválido")
    public void testCreateCpfWithInvalidValue() {
        // given
        final var expectedError = "Invalid cpf";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> new Cpf("41.536.5380001-00"));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um CPF null")
    public void testCreateCpfWithNullValue() {
        // given
        final var expectedError = "Invalid cpf";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> new Cpf(null));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}
