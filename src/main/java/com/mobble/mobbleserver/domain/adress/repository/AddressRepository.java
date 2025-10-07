package com.mobble.mobbleserver.domain.adress.repository;

import com.mobble.mobbleserver.domain.adress.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
