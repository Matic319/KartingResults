package com.maticz.Karting.results.API.repository;

import com.maticz.Karting.results.API.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client,Integer> {


    @Query(value = " select distinct idcontact from rf_ref_client_details" +
            "  where client_uuid = :clientUuid  ", nativeQuery = true )
    Integer getIdContact(@Param("clientUuid") String clientUuid);

}