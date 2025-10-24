package com.mobble.mobbleserver.infrastructure.persistence.address;

import com.mobble.mobbleserver.application.address.port.required.AddressWritePort;
import com.mobble.mobbleserver.domain.address.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AddressPersistenceAdapter implements AddressWritePort {

    private final JpaAddressRepository repository;


    @Override
    public Address save(Address address) {
        return repository.save(address);
    }
}
