package com.maticz.Karting.results.API.repository;

import com.maticz.Karting.results.API.model.FailedRunResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FailedRunResultsRepository extends JpaRepository<FailedRunResults,Integer> {

    Optional<com.maticz.Karting.results.API.model.FailedRunResults> findByIdContactAndScheduledAt(Integer idContact, LocalDateTime scheduledAt);
}
