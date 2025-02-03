package com.swiftapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Table(name = "bank")
@Getter
@Setter
@Entity
public class Bank {
    @Id
    private String SWIFTCode;
    private String codeType;
    private String bankName;
    private String address;
    private String townName;

    @ManyToOne
    @JoinColumn(name = "ISO2", referencedColumnName = "ISO2")
    private Country country;
}
