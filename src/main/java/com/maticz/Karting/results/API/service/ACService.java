package com.maticz.Karting.results.API.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface ACService {



        @Transactional
        void pastMonthResultsToAC() throws JsonProcessingException ;


        @Transactional
        void yesterdayResultsToAC() throws JsonProcessingException;


        void saveQueryToDBYesterday(String email,
                                    String speedCategory,
                                    String qualified,
                                    String timeToQualify,
                                    String timeBehindFirstRanked,
                                    String previousBestLap,
                                    String yesterdayBestLap,
                                    String improvedTime,
                                    String worsenedTime,
                                    String rankOnDatePrevBest,
                                    String rankYesterday,
                                    String allTimeBestLap
        );

        void saveQueryToDBMonthly(String email,
                                  String speedCategory,
                                  String qualified,
                                  String timeToQualify,
                                  String timeBehindFirstRanked,
                                  String bestLap,
                                  String rankOnDateOfBest,
                                  String rankToday

        );
}



