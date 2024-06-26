package com.maticz.Karting.results.API.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "rf_src_heats")
public class Heats {

    @Id
    @Column(name = "idHeat")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idheat;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "client_uuid")
    private String clientUuid;

    @Column(name="obtained")
    private LocalDateTime obtained;


}
