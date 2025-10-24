package com.mobble.mobbleserver.infrastructure.persistence.address;

import com.mobble.mobbleserver.domain.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAddressRepository extends JpaRepository<Address, Long> {
}
