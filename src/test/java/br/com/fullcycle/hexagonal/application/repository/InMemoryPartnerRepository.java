package br.com.fullcycle.hexagonal.application.repository;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryPartnerRepository implements PartnerRepository {

    private final Map<String, Partner> partners;
    private final Map<String, Partner> partnersByCnpj;
    private final Map<String, Partner> partnersByEmail;

    public InMemoryPartnerRepository() {
        this.partners = new HashMap<>();
        this.partnersByCnpj = new HashMap<>();
        this.partnersByEmail = new HashMap<>();
    }

    @Override
    public Partner create(final Partner partner) {
        this.partners.put(partner.partnerId().value().toString(), partner);
        this.partnersByCnpj.put(partner.cnpj().value(), partner);
        this.partnersByEmail.put(partner.email().value(), partner);
        return partner;
    }

    @Override
    public Partner update(Partner partner) {
        return null;
    }

    @Override
    public Optional<Partner> partnerOfCnpj(final String cnpj) {
        return Optional.ofNullable(this.partnersByCnpj.get(Objects.requireNonNull(cnpj)));
    }

    @Override
    public Optional<Partner> partnerOfEmail(final String email) {
        return Optional.ofNullable(this.partnersByEmail.get(Objects.requireNonNull(email)));
    }

    @Override
    public Optional<Partner> partnerOfId(final PartnerId id) {
        return Optional.ofNullable(this.partners.get(Objects.requireNonNull(id).value().toString()));
    }
}