package com.vanheurck.dpp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "company", schema = "dpp")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(name = "name")
    private String name;
}