package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-07-13
 * v3
 */
@Entity
@Table(name = "INVOICE",
        uniqueConstraints =
        @UniqueConstraint(
                name="IDX_UNQ_NVC_NVCNMBR",
                columnNames = {"INVOICE_NUMBER"}
        )
)
public class Invoice implements Serializable {
    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "INVOICE_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_INVOICE"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "INVOICE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "INVOICE_NUMBER", unique = true)
    @NotNull
    @Size(
            min = 0,
            max = DataBaseConstants.INVOICE_NUMBER_MAX_LGTH,
            message = "Invoice Number length is " + DataBaseConstants.INVOICE_NUMBER_MAX_LGTH + " characters."
    )
    private String invoiceNumber;

    /*
    organization cascade:
    -if invoice is deleted -> organization remains in DB
    -if organization is deleted -> exception is thrown
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ID", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DATEUNIT_UNITDAY", referencedColumnName = "UNITDAY", nullable = false)
    private DateUnit dateUnit;

    @Column(name = "COMMENT")
    @Size(
            min = 0,
            max = DataBaseConstants.INVOICE_COMMENT_MAX_LGTH,
            message = "Comment length is " + DataBaseConstants.INVOICE_COMMENT_MAX_LGTH + " characters."
    )
    private String comment;

    @Column(name = "DEBIT_TOTAL")
    private BigDecimal debitTotal = new BigDecimal("0");

    @Column(name = "CREDIT_TOTAL")
    private BigDecimal creditTotal = new BigDecimal("0");

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "T_CREATEDON", insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "T_UPDATEDON", insertable = false, updatable = false)
    private Timestamp updatedOn;

    /*
    item cascade:
    -if invoice is deleted -> items are deleted also from DB
    -if item is deleted -> invoice remains in DB
    with recalculated subtotal / total from trigger
     */
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "DATEUNIT_UNITDAY", referencedColumnName = "UNITDAY", nullable = false)
    private List<Item> items = new ArrayList<>();

    public Invoice(){

    }

    public Invoice(String invoiceNumber, Organization organization, DateUnit dateUnit, String comment) {
        this.invoiceNumber = invoiceNumber;
        this.organization = organization;
        this.dateUnit = dateUnit;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DateUnit getDateUnit() {
        return dateUnit;
    }

    public void setDateUnit(DateUnit dateUnit) {
        this.dateUnit = dateUnit;
    }

    public Long getId() {
        return id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getDebitTotal() {
        return debitTotal;
    }

    //set subtotal in item trigger

    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    //set creditTotal in item trigger

    public Collection<Item> getItems() {
        return new ArrayList<>(this.items);
    }

    public void setItems(Collection<Item> items) {
        this.items = new ArrayList<>(items);
        sumDebitTotal();
        sumCreditTotal();
    }

    public void addItem(Category category, Tax tax, FamilyMember familyMember,
                        String description1, String description2, String comment, BigDecimal subTotal){


    }

    private void sumDebitTotal() {
        this.debitTotal = items.stream()
                .filter(item -> item.getCategory().getCategoryGroupType()
                        .equals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT))
                .map(Item::getTotal)
                .reduce(
                        new BigDecimal("0"),
                        (a, b) -> a.add(b)
                );
    }

    private void sumCreditTotal() {
        this.creditTotal = items.stream()
                .filter(item -> item.getCategory().getCategoryGroupType()
                        .equals(DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT))
                .map(Item::getTotal)
                .reduce(
                        new BigDecimal("0"),
                        (a, b) -> a.add(b)
                );
    }

    private int getLastItemOrderNumber() {
        int lastItemOrderNumber = 0;
        if(items.size()>0) {
            lastItemOrderNumber = items.get(items.size() - 1).getOrderNumber();
        }
        return lastItemOrderNumber;
    }

    public int getNextItemOrderNumber() {
        return getLastItemOrderNumber() + 1;
    }

    public LocalDateTime getCreatedOn() {
        if(createdOn == null){
            return null;
        }
        return createdOn.toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        if(updatedOn == null){
            return null;
        }
        return updatedOn.toLocalDateTime();
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceNumber=" + invoiceNumber +
                ", organization=" + organization +
                ", dateUnit=" + dateUnit +
                ", comment='" + comment + '\'' +
                ", debitTotal=" + debitTotal +
                ", creditTotal=" + creditTotal +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    public String toStringEager() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceNumber=" + invoiceNumber +
                ", organization=" + organization +
                ", dateUnit=" + dateUnit +
                ", comment='" + comment + '\'' +
                ", debitTotal=" + debitTotal +
                ", creditTotal=" + creditTotal +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", items.size=" + items.size() +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Invoice) {
            Invoice that = (Invoice) other;
            return Objects.equal(getInvoiceNumber(), that.getInvoiceNumber());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInvoiceNumber());
    }
}
