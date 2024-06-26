package com.maticz.Karting.results.API.service.Impl;

import com.jayway.jsonpath.internal.function.numeric.Max;
import com.maticz.Karting.results.API.model.MaxPositionDTO;
import com.maticz.Karting.results.API.repository.RunResultsRepository;
import com.maticz.Karting.results.API.service.PositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {


    @Autowired
    RunResultsRepository runResultsRepository;

    Logger logger = LoggerFactory.getLogger(PositionServiceImpl.class);


    @Override
    public List<MaxPositionDTO> listOfUserPositions(String name) {


        List<MaxPositionDTO> resultList;

        resultList = new ArrayList<>();


      List<Object[]> data=  runResultsRepository.listOfUsersMaxCategory(name);

      for(Object[] x : data) {

          String contact = x[0].toString();
          String email = x[1].toString();
          String category = x[3].toString();

          logger.info(contact);
          logger.info(email);

          MaxPositionDTO dto = new MaxPositionDTO();

          dto.setCategory(category);
          dto.setEmail(email);
          dto.setContact(contact);

        resultList.add(dto);
      }

       return resultList;
    }

    @Override
    public List<MaxPositionDTO> listOfUserPositionsByEmail(String contactEmail) {

        List<MaxPositionDTO> resultList;

        resultList = new ArrayList<>();


        List<Object[]> data=  runResultsRepository.listOfUsersMaxCategoryByEmail(contactEmail);

        for(Object[] x : data) {

            String contact = x[0].toString();
            String email = x[1].toString();
            String category = x[3].toString();

            logger.info(contact);
            logger.info(email);

            MaxPositionDTO dto = new MaxPositionDTO();

            dto.setCategory(category);
            dto.setEmail(email);
            dto.setContact(contact);

            resultList.add(dto);
        }

        return resultList;
    }
}
