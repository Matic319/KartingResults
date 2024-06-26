package com.maticz.Karting.results.API.repository;

import com.maticz.Karting.results.API.model.Heats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HeatsRepository extends JpaRepository<Heats, Integer> {

    @Query(value = "select distinct client_uuid " +
            "from rf_src_heats " +
            "where cast(start_time as date) = :targetDate and client_uuid IS NOT NULL", nativeQuery = true)
    List<String> distinctClientsPerDay(@Param("targetDate") String date);

    Optional<Heats> findByStartTimeAndClientUuid(LocalDateTime startTime, String clientUuid);

}
