package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.EventJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class EventDatabaseRepository implements EventRepository {

    private final EventJpaRepository eventJpaRepository;

    public EventDatabaseRepository(EventJpaRepository eventJpaRepository) {
        this.eventJpaRepository = Objects.requireNonNull(eventJpaRepository);
    }

    @Override
    public Optional<Event> eventOfId(final EventId eventId) {
        Objects.requireNonNull(eventId, "eventId cannot be null");
        return this.eventJpaRepository.findById(UUID.fromString(eventId.value()))
                .map(EventEntity::toEvent);
    }


    @Override
    @Transactional
    public Event create(Event event) {
        return this.eventJpaRepository.save(EventEntity.of(event)).toEvent();
    }

    @Override
    @Transactional
    public Event update(Event customer) {
        return this.eventJpaRepository.save(EventEntity.of(customer)).toEvent();
    }

    @Override
    public void deleteAll() {
        this.eventJpaRepository.deleteAll();
    }
}
