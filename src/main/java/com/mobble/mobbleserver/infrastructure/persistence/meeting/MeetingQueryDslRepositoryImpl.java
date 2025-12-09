package com.mobble.mobbleserver.infrastructure.persistence.meeting;

import com.mobble.mobbleserver.domain.meeting.Meeting;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.mobble.mobbleserver.domain.meeting.QMeeting.meeting;

@RequiredArgsConstructor
public class MeetingQueryDslRepositoryImpl implements MeetingQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Meeting> findUpcomingMeetings(Long clubId, LocalDateTime dateTime) {
        return queryFactory
                .selectFrom(meeting)
                .where(
                        meeting.club.id.eq(clubId),
                        meeting.schedule.datetime.goe(dateTime)
                )
                .orderBy(meeting.schedule.datetime.asc())
                .fetch();
    }
}
