package com.maticz.Karting.results.API.repository;

import com.maticz.Karting.results.API.model.ClientResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClientResultsRepository extends JpaRepository<ClientResults,Integer> {

    @Query(value = " select run_uuid from " +
            " rf_src_client_results " +
            " where client_uuid like :client and cast(date_time as date) = :date ", nativeQuery = true )
    List<String> runUuidforClient(@Param("client") String clientUuid , @Param("date") String driveDate);



    Optional<ClientResults> findByRunUuid(String runUuid);



}
