package com.omkar.expensetracker.infra.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Split implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "split_id")
    private long id;

    @OneToMany(mappedBy = "split")
    private Set<Transaction> transaction;

    @OneToMany(mappedBy = "split")
    private Set<User> user;
}
