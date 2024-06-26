package com.maticz.Karting.results.API.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rf_ref_speed")
public class RefSpeed {

    @Id
    @Column(name = "idSpeedCategory")
    private Integer idSpeedCategory;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "speedset_name")
    private String speedsetName;

    @Column(name = "speedset_uuid")
    private String speedsetUuid;

    @Column(name = "query_relevant")
    private Integer queryRelevant;
}
