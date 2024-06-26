package com.maticz.Karting.results.API.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "rf_src_client_results")
public class ClientResults {

    @Id
    @Column(name = "idresult")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idresult;

    @Column(name = "date_time")
    private LocalDateTime dateTimeOfRun;

    @Column(name = "run_uuid")
    private String runUuid;

    @Column(name = "obtained")
    private LocalDateTime obtained;

    @Column(name = "client_Uuid")
    private String clientUuid;


}
