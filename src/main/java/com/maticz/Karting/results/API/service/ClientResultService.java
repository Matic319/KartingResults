package com.maticz.Karting.results.API.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maticz.Karting.results.API.model.ClientResults;
import com.maticz.Karting.results.API.repository.ClientResultsRepository;
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
public class ClientResultService {
    @Autowired
    ClientResultsRepository clientResultsRepository;

    @Value("${karting.api.authorization}")
    private String authorizationToken;
    public void getClientResults(String client) {
        RestTemplate restTemplate = new RestTemplate();
        String baseUrl = "http://192.168.10.20/api/v1/client-results?uuid=" + client + "&page=";

        Integer currentPage = 1;
        Integer totalPages = 1;

        while (currentPage <= totalPages) {
            String currentUrl = baseUrl + currentPage;

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Authorization", authorizationToken);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(currentUrl, HttpMethod.GET, entity, String.class);
                String response = responseEntity.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);

                totalPages = rootNode.path("pages").asInt();
                currentPage = rootNode.path("page").asInt();

                JsonNode dataNode = rootNode.path("data");

                for (JsonNode data : dataNode) {
                    String dateTimeRun = data.path("date_time").asText();
                    String runUuid = data.path("uuid").asText();

                    Optional<ClientResults> existingResult = clientResultsRepository.findByRunUuid(runUuid);

                    if (existingResult.isEmpty()) {
                        ClientResults clientResult = new ClientResults();
                        clientResult.setObtained(LocalDateTime.now());
                        clientResult.setRunUuid(runUuid);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        clientResult.setDateTimeOfRun(LocalDateTime.parse(dateTimeRun, formatter));
                        clientResult.setClientUuid(client);


                        clientResultsRepository.save(clientResult);

                    }
                }

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            currentPage++;
        }
    }
}