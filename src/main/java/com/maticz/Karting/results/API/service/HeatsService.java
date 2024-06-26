package com.maticz.Karting.results.API.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maticz.Karting.results.API.model.Heats;
import com.maticz.Karting.results.API.repository.HeatsRepository;
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
import java.util.Optional;

@Service
public class HeatsService {

    @Autowired
    HeatsRepository heatsRepository;

    @Value("${karting.api.authorization}")
    private String authorizationToken;

    public void getHeats(String date) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.10.20/api/v1/heats?date=";

        String wholeUrl = url + date;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", authorizationToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            wholeUrl, HttpMethod.GET, entity, String.class);
            String response = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.path("data");

            for (JsonNode data : dataNode) {
                String startTime = data.path("start_time").asText();
                Integer takenSlots = data.path("taken_slots").asInt();

                JsonNode runsData = data.path("runs").path("data");
                for (JsonNode run : runsData) {
                    String clientUuid = run.path("client_uuid").asText();

                    Heats heat = new Heats();
                    if (takenSlots >= 1) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                        Optional<Heats> existingHeat =heatsRepository.findByStartTimeAndClientUuid(LocalDateTime.parse(startTime,formatter),clientUuid);

                        if(!existingHeat.isPresent()) {

                            heat.setObtained(LocalDateTime.now());
                            heat.setStartTime(LocalDateTime.parse(startTime, formatter));
                            heat.setClientUuid(clientUuid);
                            heatsRepository.save(heat);
                        }
                    }
                }
            }
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
