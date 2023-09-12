package com.omkar.expensetracker.infra.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User creditor;  // The one who lent the money.

    @ManyToOne
    private User debtor;    // The one who owes the money.

    private BigDecimal amount;  // Amount the debtor owes to the creditor.

}
