package com.mobble.mobbleserver.infrastructure.persistence.chat.common;

import com.mobble.mobbleserver.domain.chat.room.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantQueryDslRepository {
}
