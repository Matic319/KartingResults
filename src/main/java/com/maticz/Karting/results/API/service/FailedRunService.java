package com.maticz.Karting.results.API.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maticz.Karting.results.API.repository.ClientRepository;
import com.maticz.Karting.results.API.repository.FailedRunResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class FailedRunService {

    @Autowired
    FailedRunResultsRepository failedRunResultsRepository;

    @Autowired
    ClientRepository clientRepository;

    @Value("${karting.api.authorization}")
    private String authorizationToken;

    public void getFailedRunResult(String uuid) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.10.20/api/v1/run-result?uuid=";

        String wholeUrl = url + uuid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", authorizationToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(wholeUrl, HttpMethod.GET, entity, String.class);
            String response = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode heatNode = rootNode.path("data").path("heat");
            JsonNode runNode = rootNode.path("data").path("run");

            String scheduledAt = heatNode.path("scheduled_at").asText();
            String startTime = heatNode.path("start_time").asText();
            String endTime = heatNode.path("end_time").asText();
            String trackName = heatNode.path("track_configuration").path("name").asText();

            String clientName = runNode.path("client").path("name").asText();
            String clientUuid = runNode.path("client").path("uuid").asText();
            String bestLap = runNode.path("best_lap").asText();
            Integer totalDrivers = runNode.path("total_drivers").asInt();
            String speedsetName = runNode.path("speedset").path("name").asText();
            String speedsetUuid = runNode.path("speedset").path("uuid").asText();


            //RunResults runResults = new RunResults();

            com.maticz.Karting.results.API.model.FailedRunResults runResults = new com.maticz.Karting.results.API.model.FailedRunResults();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            runResults.setObtained(LocalDateTime.now());



            boolean dontSave = false;

            if (bestLap.contains(":")) {
                Float parsed = Float.parseFloat(bestLap.substring(2));
                if (bestLap.charAt(0) == '1') {
                    runResults.setBestLap(parsed + 60);
                } else if (bestLap.charAt(0) == '2') {
                    runResults.setBestLap(parsed + 120);
                } else if (bestLap.charAt(0) == '3') {
                    runResults.setBestLap(parsed + 180);
                }
            } else if (bestLap.contains("-")) {
                runResults.setBestLap(999.999F);
                dontSave = true;
            } else {
                runResults.setBestLap(Float.parseFloat(bestLap));
            }

            //  runResults.setBestLap(Float.parseFloat(bestLap));
            runResults.setClientName(clientName);
            runResults.setTotalDrivers(totalDrivers);



            if (scheduledAt == null || scheduledAt.equals("null")) {
                runResults.setScheduledAt(LocalDateTime.of(1999, 1, 1, 1, 1, 1));
                dontSave = true;
            } else
                runResults.setScheduledAt(LocalDateTime.parse(scheduledAt, formatter));
            if (startTime == null || Objects.equals(startTime, "null")) {
                runResults.setStartTime(LocalDateTime.of(1999, 1, 1, 1, 1, 1));
                dontSave = true;
            } else
                runResults.setStartTime(LocalDateTime.parse(startTime, formatter));
            if (endTime == null || Objects.equals(endTime, "null")) {
                runResults.setStartTime(LocalDateTime.of(1999, 1, 1, 1, 1, 1));
                dontSave = true;
            } else
                runResults.setEndTime(LocalDateTime.parse(endTime, formatter));

            if (trackName == null || Objects.equals(trackName, "null")) {
                //    runResults.setTrackName(null);
                runResults.setIdTrack(0);
                dontSave = true;
            } else if (trackName.equals("Daytona")){
                runResults.setIdTrack(1);

            } else if (trackName.equals("Monaco")) {
                runResults.setIdTrack(2);
            }
                else runResults.setIdTrack(0);

            switch (speedsetUuid) {
                case "e47ecb48-12f3-47ba-80c0-d857ea202a1b" -> runResults.setIdSpeedCategory(300051);
                case "548e4827-f1b8-4c09-8155-416441b60de2", "4ea16d60-2569-11ee-b274-0d868cf2bf2f" -> runResults.setIdSpeedCategory(300052);
                case "4aec0347-1272-42ca-8d80-ffe134bcc8a7", "111713a0-43b5-11ed-8236-2f6c2c70c670" -> runResults.setIdSpeedCategory(300053);
                case "4fd32198-3975-4bf4-bd69-042383855204", "3f1316c2-4d1b-4527-ac18-8fabc4c410d5" -> runResults.setIdSpeedCategory(300054);
                case "45a127eb-9a0e-4eda-9313-60fdfea3ff9e" -> runResults.setIdSpeedCategory(300059);
                default -> {
                    dontSave = true;
                    runResults.setIdSpeedCategory(0);

                }
            }

            Integer idContact = clientRepository.getIdContact(clientUuid);

            if (idContact == 0){
                runResults.setIdContact(idContact);
                dontSave = true;

            }

            runResults.setIdContact(idContact);
            Optional<com.maticz.Karting.results.API.model.FailedRunResults> existingRuns = failedRunResultsRepository.findByIdContactAndScheduledAt( idContact, runResults.getScheduledAt());

            if(existingRuns.isEmpty() && dontSave) {
                failedRunResultsRepository.save(runResults);
            }


        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
