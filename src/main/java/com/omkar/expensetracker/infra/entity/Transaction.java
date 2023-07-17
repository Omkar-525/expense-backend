package com.omkar.expensetracker.infra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "transaction")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "type")
    private String type;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "split_id")
    private Split split;
}
