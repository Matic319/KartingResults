package com.maticz.Karting.results.API.service;

import com.maticz.Karting.results.API.model.MaxPositionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PositionService {

    public List<MaxPositionDTO> listOfUserPositions(String name);

    public List<MaxPositionDTO> listOfUserPositionsByEmail(String email);

}
