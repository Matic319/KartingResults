package com.maticz.Karting.results.API.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maticz.Karting.results.API.model.Client;
import com.maticz.Karting.results.API.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Value("${karting.api.authorization}")
    private String authorizationToken;
    public void getClient(String uuidOfClient) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.10.20/api/v1/client?uuid=";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Authorization", authorizationToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        String wholeUrl = url + uuidOfClient;

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            wholeUrl, HttpMethod.GET, entity, String.class);
            String response = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.path("data");

            String clientUuid = dataNode.path("uuid").asText();
            String clientEmail = dataNode.path("email").asText();
            Integer idContact = dataNode.path("external_id").asInt();
            String firstName = dataNode.path("first_name").asText();
            String lastName = dataNode.path("last_name").asText();
            String nickname = dataNode.path("nickname").asText();
            Integer totalHeats = dataNode.path("total_heats").asInt();
            Integer totalVisits = dataNode.path("total_visits").asInt();

            Client client = new Client();

            client.setClientUuid(clientUuid);
            client.setEmail(clientEmail);
            client.setNickname(nickname);
            client.setLastName(lastName);
            client.setIdContact(idContact);
            client.setFirstName(firstName);
            client.setTotalHeats(totalHeats);
            client.setTotalVisits(totalVisits);

            clientRepository.save(client);


        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}