package com.maticz.Karting.results.API.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "rf_ref_client_details")
public class Client {

    @Id
    @Column(name = "client_uuid")
    private String clientUuid;

    @Column(name = "idContact")
    private Integer idContact;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "total_heats")
    private Integer totalHeats;

    @Column(name = "total_visits")
    private Integer totalVisits;

}
