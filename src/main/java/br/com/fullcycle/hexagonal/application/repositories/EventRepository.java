package br.com.fullcycle.hexagonal.application.repositories;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;

import java.util.Optional;

public interface EventRepository {

    Optional<Event> eventOfId(EventId eventId);

    Event create(Event customer);

    Event update(Event customer);

}
