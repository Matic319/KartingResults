package com.maticz.Karting.results.API.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "RF_Ref_Qualify_Times")
public class RefQualifyTimes {

    @Id
    @Column(name = "idQualifyTime")
    private Integer idQualifyTime;

    @Column(name = "idSpeedCategory")
    private Integer idSpeedCategory;

    @Column(name = "qualify_time")
    private Float qualifyTime;

    @Column(name = "idTrack")
    private Integer idTrack;

    @Column(name = "dateFrom")
    private LocalDateTime dateFrom;

    @Column(name = "dateTo")
    private LocalDateTime dateTo;

}
