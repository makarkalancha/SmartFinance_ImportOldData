package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Makar Kalancha on 2016-07-14.
 */
@Entity
@Table(
        name = "TRANSACTION",
        uniqueConstraints =
        @UniqueConstraint(
                name = "IDX_UNQ_TRNSCTN_TRNSCTNNMBR",
                columnNames = {"TRANSACTION_NUMBER"}
        )
)
public class Transaction implements Serializable {
    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "TRANSACTION_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_TRANSACTION"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "TRANSACTION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "TRANSACTION_NUMBER", unique = true)
    @NotNull
    @Size(
            min = 2,
            max = DataBaseConstants.TRANSACTION_NUMBER_MAX_LGTH,
            message = "Transaction number is required, maximum " + DataBaseConstants.TRANSACTION_NUMBER_MAX_LGTH + " characters."
    )
    private String transactionNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INVOICE_ID", referencedColumnName = "ID", nullable = false)
    private Invoice invoice;
//    DATEUNIT_UNITDAY BIGINT,
//    COMMENT VARCHAR(512) NOT NULL,
//    DEBIT_AMOUNT DECIMAL(10,4) DEFAULT 0,
//    CREDIT_AMOUNT DECIMAL(10,4) DEFAULT 0,

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "T_CREATEDON", insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "T_UPDATEDON", insertable = false, updatable = false)
    private Timestamp updatedOn;
}
