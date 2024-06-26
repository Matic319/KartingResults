package com.maticz.Karting.results.API.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "AC_Log_Karting")
public class ACKartingYesterday {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name ="category_name")
    private String categoryName;

    @Column(name = "previous_best")
    private Float previousBest;

    @Column(name = "yesterdays_best")
    private Float yesterdaysBest;

    @Column(name = "improved_time")
    private Float improvedTime;

    @Column(name = "worsened_time")
    private Float worsenedTime;

    @Column(name = "rank_date_of_previous_best")
    private Integer rankPreviousBest;

    @Column(name = "rank_yesterday")
    private Integer rankYesterday;

    @Column(name = "qualified")
    private int qualified;

    @Column(name = "time_to_qualify")
    private Float timeToQualify;

    @Column(name = "time_behind_first_ranked")
    private Float timeBehindFirstRanked;

    @Column(name = "import_date")
    private LocalDateTime  date;

    @Column(name = "import_id")
    private int monthly;

    @Column(name = "all_time_best_lap")
    private Float allTimeBestLap;
}
