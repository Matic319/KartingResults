package com.maticz.Karting.results.API.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "rf_run_results_failed")
public class FailedRunResults {


        @Id
        @Column(name = "idrun")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idRun;

        @Column(name="scheduled_at")
        private LocalDateTime scheduledAt;

        @Column(name = "idTrack")
        private Integer idTrack;

        @Column(name = "best_lap")
        private Float bestLap;

        @Column(name = "obtained")
        private LocalDateTime obtained;

        @Column(name = "total_drivers")
        private Integer totalDrivers;

        @Column(name = "client_name")
        private String clientName;

        @Column(name = "start_time")
        private LocalDateTime startTime;

        @Column(name = "end_time")
        private LocalDateTime endTime;

        @Column(name = "idSpeedCategory")
        private Integer idSpeedCategory;

        @Column(name = "idContact")
        private Integer idContact;

        @Column(name = "api_endpoint")
        private String endPointURL;

        @Column(name = "response_body")
        private String responseBody;


    }

