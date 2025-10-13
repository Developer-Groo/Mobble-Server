package com.mobble.mobbleserver.refactor.adress.repository;

import com.mobble.mobbleserver.refactor.adress.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
