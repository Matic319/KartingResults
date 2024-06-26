package com.maticz.Karting.results.API.repository;

import com.maticz.Karting.results.API.model.ACKartingYesterday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ACKartingYesterdayRepository extends JpaRepository<ACKartingYesterday,Integer> {
}
