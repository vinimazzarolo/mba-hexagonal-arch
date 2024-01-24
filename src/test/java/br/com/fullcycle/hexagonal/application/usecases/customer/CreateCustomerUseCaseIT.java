package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.CustomerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.CustomerJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class CreateCustomerUseCaseIT extends IntegrationTest {

    @Autowired
    private CreateCustomerUseCase useCase;

    @Autowired
    private CustomerJpaRepository customerJpaRepository;

    @AfterEach
    void tearDown() {
        customerJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreateCustomer() {
        // given
        final var expectedCpf = "12345678901";
        final var expectedName = "Vini";
        final var expectedEmail = "vini@vini.com";
        final var createInput = new CreateCustomerUseCase.Input(expectedCpf, expectedEmail, expectedName);

        // when
        final var output = useCase.execute(createInput);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCpf, output.cpf());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws Exception {
        // given
        final var expectedCpf = "12345678901";
        final var expectedName = "Vini";
        final var expectedEmail = "vini@vini.com";
        final var expectedError = "Customer already exists";
        createCustomer(expectedCpf, expectedName, expectedEmail);
        final var createInput = new CreateCustomerUseCase.Input(expectedCpf, expectedEmail, expectedName);


        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        // given
        final var expectedCpf = "12345678901";
        final var expectedName = "Vini";
        final var expectedEmail = "vini@vini.com";
        final var expectedError = "Customer already exists";
        createCustomer("12345678921", expectedName, expectedEmail);
        final var createInput = new CreateCustomerUseCase.Input(expectedCpf, expectedEmail, expectedName);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private CustomerEntity createCustomer(final String cpf, final String email, final String name) {
        final var aCustomer = new CustomerEntity();
        aCustomer.setCpf(cpf);
        aCustomer.setName(email);
        aCustomer.setEmail(name);
        return customerJpaRepository.save(aCustomer);
    }
}
