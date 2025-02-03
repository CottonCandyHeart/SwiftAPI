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
    @Column
    private String codeType;
    @Column
    private String bankName;
    @Column
    private String address;
    @Column
    private String townName;
    @Column
    private boolean isHeadquarter;
    @ManyToOne
    @JoinColumn(name = "ISO2", referencedColumnName = "ISO2")
    private Country country;
}
