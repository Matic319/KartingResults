package com.maticz.Karting.results.API.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maticz.Karting.results.API.AC.UpdateACServiceMonthly;
import com.maticz.Karting.results.API.model.MaxPositionDTO;
import com.maticz.Karting.results.API.repository.ClientRepository;
import com.maticz.Karting.results.API.repository.ClientResultsRepository;
import com.maticz.Karting.results.API.repository.HeatsRepository;
import com.maticz.Karting.results.API.service.*;
import com.maticz.Karting.results.API.service.Impl.ACServiceImpl;
import com.maticz.Karting.results.API.service.Impl.PositionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/karting")
@CrossOrigin
public class KartingController {

    @Autowired
    HeatsService heatsService;

    @Autowired
    ClientResultService clientResultService;

    @Autowired
    RunResultsService runResultsService;
    @Autowired
    HeatsRepository heatsRepository;

    @Autowired
    ClientResultsRepository clientResultsRepository;

    @Autowired
    UpdateACServiceMonthly updateACServiceMonthly;

    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ACServiceImpl acService;

    @Autowired
    PositionServiceImpl positionService;

    @Autowired
    FailedRunService failedRunService;


    LocalDate setDate = LocalDate.of(2024, 3, 28);


    @GetMapping("/heats")
    ResponseEntity<String> getHeats() {

        heatsService.getHeats("2023-10-21");
        return ResponseEntity.ok("ok");
    }


    @GetMapping("/runResult")
    ResponseEntity<String> getRun() {

        runResultsService.getRunResult("4fb9d54a-1f19-4214-9183-a2b643c8105f");

        return ResponseEntity.ok("ok");
    }

    //@Scheduled(cron = "0 15 9 21 11 ?")
    @GetMapping("/resultsForSetDate")
    ResponseEntity<String> resultsForSetDate() {

        while (setDate.isBefore(LocalDate.now())) {

            heatsService.getHeats(setDate.toString());
            List<String> clientList = heatsRepository.distinctClientsPerDay(setDate.toString());

            for (String clientId : clientList) {
                clientResultService.getClientResults(clientId);
                clientService.getClient(clientId);


                List<String> runUuidList = clientResultsRepository.runUuidforClient(clientId, setDate.toString());

                for (String rundId : runUuidList) {

                    runResultsService.getRunResult(rundId);

                }

            }

            setDate = setDate.plusDays(1);
        }
        return ResponseEntity.ok("saved");
    }


    @Scheduled(cron = "0 0 6 * * *")
    @GetMapping("/resultsYesterday")
    ResponseEntity<String> resultsForDate() {


            String yesterday =  LocalDate.now().minusDays(1).toString();

            heatsService.getHeats(yesterday);
            List<String> clientList = heatsRepository.distinctClientsPerDay(yesterday);

            for (String clientId : clientList) {
                clientResultService.getClientResults(clientId);
                clientService.getClient(clientId);


                List<String> runUuidList = clientResultsRepository.runUuidforClient(clientId, yesterday);

                for (String rundId : runUuidList) {

                    runResultsService.getRunResult(rundId);
                }
            }

        return ResponseEntity.ok("saved");
    }


    @GetMapping("/failedresultsFor{date}")
    ResponseEntity<String> failedResults(@RequestParam() String date) {

        while (setDate.isBefore(LocalDate.now())) {

            heatsService.getHeats(setDate.toString());
            List<String> clientList = heatsRepository.distinctClientsPerDay(setDate.toString());

            for (String clientId : clientList) {
                clientResultService.getClientResults(clientId);
                clientService.getClient(clientId);


                List<String> runUuidList = clientResultsRepository.runUuidforClient(clientId, setDate.toString());

                for (String rundId : runUuidList) {

                    failedRunService.getFailedRunResult(rundId);
                }
            }
            setDate = setDate.plusDays(1);
        }
        return ResponseEntity.ok("k");
    }

    @Scheduled(cron ="0 10 6 * * *" )
    @GetMapping("/saveToAc")
    ResponseEntity<String> saveToAc() throws JsonProcessingException {
        acService.pastMonthResultsToAC();

        return ResponseEntity.ok("saved");
    }



    @GetMapping("/resultsFromDate{date}")
    ResponseEntity<String> resultsFromDate(@RequestParam() String date) {

        LocalDate fromDate = LocalDate.parse(date);


        while (fromDate.isBefore(LocalDate.now())) {

            heatsService.getHeats(fromDate.toString());
            List<String> clientList = heatsRepository.distinctClientsPerDay(fromDate.toString());

            for (String clientId : clientList) {
                clientResultService.getClientResults(clientId);
                clientService.getClient(clientId);


                List<String> runUuidList = clientResultsRepository.runUuidforClient(clientId, fromDate.toString());

                for (String rundId : runUuidList) {

                    runResultsService.getRunResult(rundId);
                }
            }

            setDate = setDate.plusDays(1);
        }
        return ResponseEntity.ok("Obtained results from date: " + date);
    }


    @Scheduled(cron ="0 15 6 * * *" )
    @GetMapping("/yesterdaysResultsToAC")
    public ResponseEntity<String> yesterdaysResultsToAC() throws JsonProcessingException {

        acService.yesterdayResultsToAC();
        return ResponseEntity.ok("ok");

    }

    @GetMapping("/getPosition")
    public ResponseEntity<List<MaxPositionDTO>> getUserPosition(String name) {
        List<MaxPositionDTO> result = positionService.listOfUserPositions(name);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/getPositionByEmail")
    public ResponseEntity<List<MaxPositionDTO>> getUserPositionByEmail(String email) {
        List<MaxPositionDTO> result = positionService.listOfUserPositionsByEmail(email);

        return ResponseEntity.ok(result);
    }

}