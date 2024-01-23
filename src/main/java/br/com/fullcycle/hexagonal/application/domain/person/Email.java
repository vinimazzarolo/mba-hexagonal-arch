package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Email (String value) {

    public Email {
        if (value == null || !value.matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$")) {
            throw new ValidationException("Invalid email");
        }
    }

}
