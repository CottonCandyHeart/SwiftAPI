package com.swiftapi.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Table(name = "country")
@Getter
@Setter
@Entity
public class Country {
    @Id
    private String ISO2;
    @Column
    private String countryName;
    @Column
    private String TimeZone;
}
