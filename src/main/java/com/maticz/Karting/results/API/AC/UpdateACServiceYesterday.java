package com.maticz.Karting.results.API.AC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UpdateACServiceYesterday {

        Logger logger = LoggerFactory.getLogger(com.maticz.Karting.results.API.AC.UpdateACServiceYesterday.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        @Value("${ac.api.token}")
        private String acToken;

        public void updateAC(String email, String prevBestLap, String speedCategory, String qualified, String timeToQualify, String worsenedBestLap, String improvedBestLap, String yesterdaysBestLap, String timeBehindFirstRanked, String prevBestRank, String currentRank, String bestLap) throws JsonProcessingException {

            RestTemplate restTemplate = new RestTemplate();
            String baseUrl = "https://woop.activehosted.com/api/3/contact/sync";

            HttpHeaders headers = new HttpHeaders();
            headers.set("API-Token", acToken);

            List<Map<String, Object>> fieldValues = new ArrayList<>();
            fieldValues.add(createFieldValueMap("270", bestLap));
            fieldValues.add(createFieldValueMap("271", speedCategory));
            fieldValues.add(createFieldValueMap("272", qualified));
            fieldValues.add(createFieldValueMap("275", timeToQualify));
            fieldValues.add(createFieldValueMap("281","0")); // monthly = 0
            fieldValues.add(createFieldValueMap("283",worsenedBestLap));
            fieldValues.add(createFieldValueMap("280", improvedBestLap));
            fieldValues.add(createFieldValueMap("284", yesterdaysBestLap));
            fieldValues.add(createFieldValueMap("286", timeBehindFirstRanked));
            fieldValues.add(createFieldValueMap("278", LocalDate.now().toString().formatted(formatter)));
            fieldValues.add(createFieldValueMap("273", currentRank));
            fieldValues.add(createFieldValueMap("274", prevBestRank));
            fieldValues.add(createFieldValueMap("287", prevBestLap));

            Map<String, Object> contact = new HashMap<>();
            contact.put("email", email);
            contact.put("fieldValues", fieldValues);

            Map<String, Object> body = new HashMap<>();
            body.put("contact", contact);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(body);
            logger.info("Final JSON body sent: {}", json);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);
            logger.info("API Response: {}", responseEntity.getBody());
        }

        private Map<String, Object> createFieldValueMap(String fieldId, String value) {
            Map<String, Object> fieldValueMap = new HashMap<>();
            fieldValueMap.put("field", fieldId);
            fieldValueMap.put("value", value != null ? value : "");
            return fieldValueMap;
        }
    }

