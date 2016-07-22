package com.makco.smartfinance.persistence.entity.session.invoice_management.v1;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.Account;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.entity.Invoice;

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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Makar Kalancha on 2016-07-18.
 * v1
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
public class Transaction_v1 implements Serializable {
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
    private Invoice_v1 invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DATEUNIT_UNITDAY", referencedColumnName = "UNITDAY", nullable = false)
    private DateUnit dateUnit;

    @Column(name = "COMMENT")
    @Size(
            min = 0,
            max = DataBaseConstants.TRANSACTION_COMMENT_MAX_LGTH,
            message = "Comment length is " + DataBaseConstants.TRANSACTION_COMMENT_MAX_LGTH + " characters."
    )
    private String comment;

    @Column(name = "DEBIT_AMOUNT")
    private BigDecimal debitAmount = new BigDecimal("0");

    @Column(name = "CREDIT_AMOUNT")
    private BigDecimal creditAmount = new BigDecimal("0");

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "T_CREATEDON", insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "T_UPDATEDON", insertable = false, updatable = false)
    private Timestamp updatedOn;

    public Transaction_v1() {

    }

    public Transaction_v1(String transactionNumber, Account account, Invoice_v1 invoice, DateUnit dateUnit, String comment,
                          BigDecimal debitAmount, BigDecimal creditAmount) {
        this.transactionNumber = transactionNumber;
        this.account = account;
        this.invoice = invoice;
        this.dateUnit = dateUnit;
        this.comment = comment;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public DateUnit getDateUnit() {
        return dateUnit;
    }

    public void setDateUnit(DateUnit dateUnit) {
        this.dateUnit = dateUnit;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Long getId() {
        return id;
    }

    //no setId method

    public Invoice_v1 getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice_v1 invoice) {
        this.invoice = invoice;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn.toLocalDateTime();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionNumber='" + transactionNumber + '\'' +
                ", account=" + account +
                ", invoice=" + invoice +
                ", dateUnit=" + dateUnit +
                ", comment='" + comment + '\'' +
                ", debitAmount=" + debitAmount +
                ", creditAmount=" + creditAmount +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Transaction_v1) {
            Transaction_v1 that = (Transaction_v1) other;
            return Objects.equal(getTransactionNumber(), that.getTransactionNumber());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTransactionNumber());
    }
}
