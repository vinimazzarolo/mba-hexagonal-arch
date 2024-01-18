package br.com.fullcycle.hexagonal.application.usecases;

import br.com.fullcycle.hexagonal.infrastructure.models.Customer;
import br.com.fullcycle.hexagonal.infrastructure.models.Event;
import br.com.fullcycle.hexagonal.infrastructure.models.Ticket;
import br.com.fullcycle.hexagonal.infrastructure.models.TicketStatus;
import br.com.fullcycle.hexagonal.infrastructure.services.CustomerService;
import br.com.fullcycle.hexagonal.infrastructure.services.EventService;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubscribeCustomerToEventUseCaseTest {
    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {
        // given
        final var expectedTicketsSize = 1;
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventId);
        aEvent.setName("Disney on Ice");
        aEvent.setTotalSpots(10);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, aEvent.getId());

        // when
        final var eventService = mock(EventService.class);
        final var customerService = mock(CustomerService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.empty());
        when(eventService.save(aEvent)).thenAnswer(a -> {
            final var event = a.getArgument(0, Event.class);
            Assertions.assertEquals(expectedTicketsSize, event.getTickets().size());
            return event;
        });

        final var useCase = new SubscribeCustomerToEventUseCase(eventService, customerService);
        final var output = useCase.execute(subscribeInput);

        // then
        Assertions.assertEquals(eventId, output.eventId());
        Assertions.assertNotNull(output.reservationDate());
        Assertions.assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWhitoutEvent() throws Exception {
        // given
        final var expectedError = "Event not found";
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, eventId);

        // when
        final var eventService = mock(EventService.class);
        final var customerService = mock(CustomerService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventId)).thenReturn(Optional.empty());

        final var useCase = new SubscribeCustomerToEventUseCase(eventService, customerService);
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket com um cliente que não existe")
    public void testReserveTicketWhitoutCustomer() throws Exception {
        // given
        final var expectedError = "Customer not found";
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, eventId);

        // when
        final var eventService = mock(EventService.class);
        final var customerService = mock(CustomerService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.empty());

        final var useCase = new SubscribeCustomerToEventUseCase(eventService, customerService);
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não pode comprar mais de 1 ticket por evento")
    public void testReserveTicketMoreThanOnce() throws Exception {
        // given
        final var expectedError = "Email already registered";
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventId);
        aEvent.setName("Disney on Ice");
        aEvent.setTotalSpots(10);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, aEvent.getId());

        // when
        final var eventService = mock(EventService.class);
        final var customerService = mock(CustomerService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.of(new Ticket()));

        final var useCase = new SubscribeCustomerToEventUseCase(eventService, customerService);
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Um cliente não pode comprar de um evento que não há mais cadeiras")
    public void testReserveTicketWithoutSlots() throws Exception {
        // given
        final var expectedError = "Event sold out";
        final var customerId = TSID.fast().toLong();
        final var eventId = TSID.fast().toLong();

        final var aEvent = new Event();
        aEvent.setId(eventId);
        aEvent.setName("Disney on Ice");
        aEvent.setTotalSpots(0);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(customerId, aEvent.getId());

        // when
        final var eventService = mock(EventService.class);
        final var customerService = mock(CustomerService.class);

        when(customerService.findById(customerId)).thenReturn(Optional.of(new Customer()));
        when(eventService.findById(eventId)).thenReturn(Optional.of(aEvent));
        when(eventService.findTicketByEventIdAndCustomerId(eventId, customerId)).thenReturn(Optional.empty());

        final var useCase = new SubscribeCustomerToEventUseCase(eventService, customerService);
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}