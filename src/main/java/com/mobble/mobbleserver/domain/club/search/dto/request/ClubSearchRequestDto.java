package com.mobble.mobbleserver.domain.club.search.dto.request;

public record ClubSearchRequestDto(
        String name,
        Long groundCode,
        Double latitude,
        Double longitude,
        Double distanceKm,
        String category,
        String ageGroup,
        Boolean isAutoJoin,
        String sort
){

}
