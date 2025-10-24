package com.mobble.mobbleserver.application.address.port.required;

import com.mobble.mobbleserver.domain.address.Address;

public interface AddressWritePort {

    Address save(Address address);
}
