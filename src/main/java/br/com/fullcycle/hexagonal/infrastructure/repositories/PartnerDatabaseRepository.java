package br.com.fullcycle.hexagonal.infrastructure.repositories;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.hexagonal.infrastructure.jpa.repositories.PartnerJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
public class PartnerDatabaseRepository implements PartnerRepository {

    private final PartnerJpaRepository partnerJpaRepository;

    public PartnerDatabaseRepository(PartnerJpaRepository partnerJpaRepository) {
        this.partnerJpaRepository = Objects.requireNonNull(partnerJpaRepository);
    }

    @Override
    public Optional<Partner> partnerOfId(final PartnerId partnerId) {
        Objects.requireNonNull(partnerId, "partnerId cannot be null");
        return this.partnerJpaRepository.findById(UUID.fromString(partnerId.value()))
                .map(PartnerEntity::toPartner);
    }

    @Override
    public Optional<Partner> partnerOfCnpj(final Cnpj cnpj) {
        Objects.requireNonNull(cnpj, "CNPJ cannot be null");
        return this.partnerJpaRepository.findByCnpj(cnpj.value())
                .map(PartnerEntity::toPartner);
    }

    @Override
    public Optional<Partner> partnerOfEmail(final Email email) {
        Objects.requireNonNull(email, "Email cannot be null");
        return this.partnerJpaRepository.findByEmail(email.value())
                .map(PartnerEntity::toPartner);
    }

    @Override
    @Transactional
    public Partner create(Partner customer) {
        return this.partnerJpaRepository.save(PartnerEntity.of(customer)).toPartner();
    }

    @Override
    @Transactional
    public Partner update(Partner customer) {
        return this.partnerJpaRepository.save(PartnerEntity.of(customer)).toPartner();
    }

    @Override
    public void deleteAll() {
        this.partnerJpaRepository.deleteAll();
    }
}
