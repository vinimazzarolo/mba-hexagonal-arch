package br.com.fullcycle.hexagonal.application.person;

import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CnpjTest {

    @Test
    @DisplayName("Deve instanciar um CNPJ")
    public void testeCreateCnpj() {
        // given
        final var expectedCnpj = "41.536.538/0001-00";

        // when
        final var actualCnpj = new Cnpj(expectedCnpj);

        // then
        Assertions.assertEquals(expectedCnpj, actualCnpj.value());
    }

    @Test
    @DisplayName("Não deve instanciar um CNPJ inválido")
    public void testCreateCnpjWithInvalidValue() {
        // given
        final var expectedError = "Invalid cnpj";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> new Cnpj("41.536.5380001-00"));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um CNPJ null")
    public void testCreateCnpjWithNullValue() {
        // given
        final var expectedError = "Invalid cnpj";

        // when
        final var actualError = Assertions.assertThrows(ValidationException.class, () -> new Cnpj(null));

        // then
        Assertions.assertEquals(expectedError, actualError.getMessage());
    }
}
