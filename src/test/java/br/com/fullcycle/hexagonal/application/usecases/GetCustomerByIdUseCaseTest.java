package br.com.fullcycle.hexagonal.application.usecases;


import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.models.Customer;
import br.com.fullcycle.hexagonal.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetCustomerByIdUseCaseTest {
    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGetById() {
        // given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();
        final var expectedCpf = "12345678901";
        final var expectedEmail = "vini@vini.com";
        final var expectedName = "Vini";

        final var aCustomer = new Customer();
        aCustomer.setId(expectedId);
        aCustomer.setCpf(expectedCpf);
        aCustomer.setName(expectedName);
        aCustomer.setEmail(expectedEmail);

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        when(customerService.findById(expectedId)).thenReturn(java.util.Optional.of(aCustomer));

        final var useCase = new GetCustomerByIdUseCase(customerService);
        final var output = useCase.execute(input).get();

        // then
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedCpf, output.cpf());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedEmail, output.email());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um cliente não existente por id")
    public void testGetByIdWithInvalidId() {
        // given
        final var expectedId = UUID.randomUUID().getMostSignificantBits();

        final var input = new GetCustomerByIdUseCase.Input(expectedId);

        // when
        final var customerService = Mockito.mock(CustomerService.class);
        when(customerService.findById(expectedId)).thenReturn(java.util.Optional.empty());

        final var useCase = new GetCustomerByIdUseCase(customerService);
        final var output = useCase.execute(input);

        // then
        Assertions.assertTrue(output.isEmpty());
    }
}