package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customers;
    private final Map<String, Customer> customersByCpf;
    private final Map<String, Customer> customersByEmail;

    public InMemoryCustomerRepository() {
        this.customers = new HashMap<>();
        this.customersByCpf = new HashMap<>();
        this.customersByEmail = new HashMap<>();
    }

    @Override
    public Customer create(final Customer customer) {
        this.customers.put(customer.customerId().value().toString(), customer);
        this.customersByCpf.put(customer.cpf().value(), customer);
        this.customersByEmail.put(customer.email().value(), customer);
        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public Optional<Customer> customerOfCpf(final Cpf cpf) {
        return Optional.ofNullable(this.customersByCpf.get(Objects.requireNonNull(cpf.value())));
    }

    @Override
    public Optional<Customer> customerOfEmail(final Email email) {
        return Optional.ofNullable(this.customersByEmail.get(Objects.requireNonNull(email.value())));
    }

    @Override
    public Optional<Customer> customerOfId(final CustomerId id) {
        return Optional.ofNullable(this.customers.get(Objects.requireNonNull(id).value().toString()));
    }
}