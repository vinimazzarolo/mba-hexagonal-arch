package br.com.fullcycle.hexagonal.application.domain;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event {

    private final EventId eventId;
    private Name name;
    private LocalDate date;
    private int totalSpots;
    private PartnerId partnerId;

    public Event(EventId eventId, String name, String date, Integer totalSpots, PartnerId partnerId) {
        if (eventId == null) {
            throw new ValidationException("Invalid value for EventId");
        }

        this.eventId = eventId;
        this.setName(name);
        this.setDate(date);
        this.setTotalSpots(totalSpots);
        this.setPartnerId(partnerId);
    }

    public static Event newEvent(final String name, final String date, final Integer totalSpots, final Partner partner) {
        return new Event(EventId.unique(), name, date, totalSpots, partner.partnerId());
    }

    public EventId eventId() {
        return eventId;
    }

    public Name name() {
        return name;
    }

    public LocalDate date() {
        return date;
    }

    public int totalSpots() {
        return totalSpots;
    }

    public PartnerId partnerId() {
        return partnerId;
    }

    public void setName(final String name) {
        this.name = new Name(name);
    }

    public void setDate(final String date) {
        if (date == null) {
            throw new ValidationException("Invalid value for date");
        }
        this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void setTotalSpots(final Integer totalSpots) {
        if (totalSpots == null || totalSpots <= 0) {
            throw new ValidationException("Invalid value for totalSpots");
        }
        this.totalSpots = totalSpots;
    }

    public void setPartnerId(final PartnerId partnerId) {
        if (partnerId == null) {
            throw new ValidationException("Invalid value for partnerId");
        }
        this.partnerId = partnerId;
    }
}
