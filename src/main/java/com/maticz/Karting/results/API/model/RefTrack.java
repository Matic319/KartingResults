package com.maticz.Karting.results.API.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rf_ref_track")
public class RefTrack {

    @Id
    @Column(name = "idTrack")
    private Integer idTrack;

    @Column(name = "track_name")
    private String trackName;

    @Column(name = "rf_track_uuid")
    private String rfTrackUuid;

    @Column(name = "active")
    private Integer active;

}
