package com.maticz.Karting.results.API.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maticz.Karting.results.API.AC.UpdateACServiceMonthly;
import com.maticz.Karting.results.API.AC.UpdateACServiceYesterday;
import com.maticz.Karting.results.API.model.ACKartingYesterday;
import com.maticz.Karting.results.API.repository.ACKartingYesterdayRepository;
import com.maticz.Karting.results.API.repository.RunResultsRepository;
import com.maticz.Karting.results.API.service.ACService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ACServiceImpl implements ACService {

    Logger logger = LoggerFactory.getLogger(ACService.class);

    @Autowired
    RunResultsRepository runResultsRepository;

    @Autowired
    UpdateACServiceMonthly updateACServiceMonthly;

    @Autowired
    UpdateACServiceYesterday updateACServiceYesterday;

    @Autowired
    ACKartingYesterdayRepository acKartingYesterdayRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    @Override
    public void pastMonthResultsToAC() throws JsonProcessingException {
        List<Object[]> data = runResultsRepository.resultsForPastMonth();

        String timeToQualify = " ";

        for (Object[] row : data) {

            String contactEmail = row[8].toString();
            String speedCategory = row[4].toString();
            String qualified = row[5].toString();
            String rankToday = row[11].toString();
            String bestRank = row[10].toString();
            String bestLap = row[9].toString();
            if(row[6] == null) {
                timeToQualify = " ";
            } else {
                BigDecimal bd = new BigDecimal(Float.parseFloat(row[6].toString()));
                bd = bd.setScale(3, RoundingMode.HALF_UP);
                timeToQualify = bd.toString();
            }
            String timeTillFirstRanked;
            try {
             timeTillFirstRanked = row[7].toString();
            }catch (NullPointerException | NumberFormatException e) {
                timeTillFirstRanked = null;
            }
            saveQueryToDBMonthly(contactEmail,speedCategory,qualified,timeToQualify,timeTillFirstRanked,bestLap,bestRank,rankToday);
            updateACServiceMonthly.updateAC(contactEmail, bestLap, speedCategory,qualified,rankToday,bestRank,timeToQualify );
        }
    }

    @Override
    public void yesterdayResultsToAC() throws JsonProcessingException {

        String timeToQualify;

        List<Object[]>  data = runResultsRepository.yesterdayResult();

        for(Object[] row : data) {

            String speedCategory = row[0].toString();
            String qualified = row[1].toString();

            if(row[2]== null) {
                timeToQualify = " ";
            }else {
                BigDecimal bd = new BigDecimal(Float.parseFloat(row[2].toString()));
                bd = bd.setScale(3, RoundingMode.HALF_UP);
                timeToQualify = bd.toString();
            }


            String timeBehindFirstRanked = row[3].toString();
            String contactEmail = row[4].toString();
            String previousBestLap;
            try {
                previousBestLap= row[5].toString(); }
            catch (NullPointerException e) {
                previousBestLap = null;
            }
            String yesterdaysBestLap = row[6].toString();

            BigDecimal bdImprove = new BigDecimal(Float.parseFloat(row[7].toString()));
            bdImprove = bdImprove.setScale(3, RoundingMode.HALF_UP);
            String improvedTimeBy = bdImprove.toString();

            BigDecimal bdWorse = new BigDecimal(Float.parseFloat(row[8].toString()));
            bdWorse = bdWorse.setScale(3, RoundingMode.HALF_UP);
            String worsenedTimeBy = bdWorse.toString();

            String rankOnDayOfPreviousBest;
            try {
                rankOnDayOfPreviousBest = row[9].toString();
            } catch (NullPointerException e) {
                rankOnDayOfPreviousBest = null;
            }
            String rankYesterday = row[10].toString();

            String allTimeBestLap = row[11].toString();


            saveQueryToDBYesterday(contactEmail,speedCategory,qualified,timeToQualify,timeBehindFirstRanked,previousBestLap,yesterdaysBestLap,improvedTimeBy,worsenedTimeBy,rankOnDayOfPreviousBest,rankYesterday,allTimeBestLap);


            updateACServiceYesterday.updateAC(contactEmail, previousBestLap,speedCategory,qualified,timeToQualify,worsenedTimeBy,improvedTimeBy,yesterdaysBestLap,timeBehindFirstRanked,rankOnDayOfPreviousBest,rankYesterday,allTimeBestLap);

        }



    }

    @Override
    public void saveQueryToDBYesterday(String email,
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
    ) {

        ACKartingYesterday yesterday = new ACKartingYesterday();

        yesterday.setDate(LocalDateTime.now());
        yesterday.setEmail(email);
        yesterday.setRankYesterday(Integer.valueOf(rankYesterday));
        yesterday.setQualified(Integer.parseInt(qualified));
        yesterday.setCategoryName(speedCategory);

        Float floatTimeToQualify;

        try {
            floatTimeToQualify = Float.parseFloat(timeToQualify);
        } catch (NullPointerException | NumberFormatException e ) {

            floatTimeToQualify = null;
        }

        yesterday.setTimeToQualify(floatTimeToQualify);

        Float floatTimeBehindFirstRanked;

        try {
            floatTimeBehindFirstRanked = Float.parseFloat(timeBehindFirstRanked);
        }catch (NumberFormatException | NullPointerException e) {
            floatTimeBehindFirstRanked = null;
        }

        yesterday.setTimeBehindFirstRanked(floatTimeBehindFirstRanked);

        Float floatPreviousBestLap;

        try {
            floatPreviousBestLap = Float.parseFloat(previousBestLap);
        }catch (NullPointerException | NumberFormatException e  ) {
            floatPreviousBestLap = null;
        }


        yesterday.setPreviousBest(floatPreviousBestLap);
        yesterday.setYesterdaysBest(Float.valueOf(yesterdayBestLap));
        yesterday.setImprovedTime(Float.valueOf(improvedTime));
        yesterday.setWorsenedTime(Float.valueOf(worsenedTime));


        Integer intRankOnDatePrevBest;

        try {
            intRankOnDatePrevBest = Integer.parseInt(rankOnDatePrevBest);
        } catch ( NullPointerException | NumberFormatException e ) {
            intRankOnDatePrevBest = null;
        }


        yesterday.setRankPreviousBest(intRankOnDatePrevBest);
        yesterday.setMonthly(0);

        Float allTimeBest;

        try {
            allTimeBest = Float.parseFloat(allTimeBestLap);
        }catch ( NullPointerException | NumberFormatException e) {
            allTimeBest = null;
        }

        yesterday.setAllTimeBestLap(allTimeBest);

        acKartingYesterdayRepository.save(yesterday);


    }


    @Override
    public void saveQueryToDBMonthly(String email,
                                     String speedCategory,
                                     String qualified,
                                     String timeToQualify,
                                     String timeBehindFirstRanked,
                                     String bestLap,
                                     String rankOnDateOfBest,
                                     String rankToday

    ) {

        ACKartingYesterday monthly = new ACKartingYesterday();

        monthly.setDate(LocalDateTime.now());
        monthly.setEmail(email);
        monthly.setQualified(Integer.parseInt(qualified));
        monthly.setCategoryName(speedCategory);

        Float floatTimeToQualify;

        try {
            floatTimeToQualify = Float.parseFloat(timeToQualify);
        } catch (NullPointerException | NumberFormatException e ) {

            floatTimeToQualify = null;
        }

        monthly.setTimeToQualify(floatTimeToQualify);
        Float floatTimeBehindFirstRanked;

        try {
            floatTimeBehindFirstRanked = Float.parseFloat(timeBehindFirstRanked);
        }catch (NumberFormatException | NullPointerException e) {
            floatTimeBehindFirstRanked = null;
        }

        monthly.setTimeBehindFirstRanked(floatTimeBehindFirstRanked);

        Float floatBestLap;

        try {
            floatBestLap = Float.parseFloat(bestLap);
        }catch (NullPointerException | NumberFormatException e  ) {
            floatBestLap = null;
        }

        monthly.setAllTimeBestLap(floatBestLap);

        Integer intRankOnDateOfBest;

        try {
            intRankOnDateOfBest = Integer.parseInt(rankOnDateOfBest);
        } catch ( NullPointerException | NumberFormatException e ) {
            intRankOnDateOfBest = null;
        }


        monthly.setRankPreviousBest(intRankOnDateOfBest);
        monthly.setMonthly(1);

        acKartingYesterdayRepository.save(monthly);


    }
}
