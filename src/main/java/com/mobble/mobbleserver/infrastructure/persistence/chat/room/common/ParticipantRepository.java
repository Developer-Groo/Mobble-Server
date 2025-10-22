package com.mobble.mobbleserver.infrastructure.persistence.chat.room.common;

import com.mobble.mobbleserver.domain.chat.room.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantQueryDslRepository {
}
