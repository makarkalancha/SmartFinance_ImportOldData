package com.makco.smartfinance.persistence.entity.session.invoice_management.v1;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.persistence.entity.DateUnit;
import com.makco.smartfinance.persistence.entity.Item;
import com.makco.smartfinance.persistence.entity.Organization;
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
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-07-18
 * v1
 */
@Entity
@Table(name = "INVOICE",
        uniqueConstraints =
        @UniqueConstraint(
                name="IDX_UNQ_NVC_NVCNMBR",
                columnNames = {"INVOICE_NUMBER"}
        )
)
public class Invoice_v1 implements Serializable {
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
    private Organization_v1 organization;

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

    /*
    investigate
    D:\SRC\smartFinance_workspace\SmartFinance\src\test\java\com\makco\smartfinance\persistence\entity\session\invoice_management\v1\Invoice_v1_query.ods
     */
    //one extra select query to fetch subtotal and total
//    @org.hibernate.annotations.Generated(
//            GenerationTime.ALWAYS
//    )
    @Column(name = "SUB_TOTAL", insertable = false, updatable = false)
    private BigDecimal subTotal = new BigDecimal("0");

    /*
    investigate
    D:\SRC\smartFinance_workspace\SmartFinance\src\test\java\com\makco\smartfinance\persistence\entity\session\invoice_management\v1\Invoice_v1_query.ods
    */
    //one extra select query to fetch subtotal and total
//    @org.hibernate.annotations.Generated(
//            GenerationTime.ALWAYS
//    )
    @Column(name = "TOTAL", insertable = false, updatable = false)
    private BigDecimal total = new BigDecimal("0");

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
    private List<Item_v1> items = new ArrayList<>();

    public Invoice_v1(){

    }

    public Invoice_v1(String invoiceNumber, Organization_v1 organization, DateUnit dateUnit, String comment) {
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

    public Organization_v1 getOrganization() {
        return organization;
    }

    public void setOrganization(Organization_v1 organization) {
        this.organization = organization;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    //set subtotal in item trigger

    public BigDecimal getTotal() {
        return total;
    }

    //set total in item trigger

    public Collection<Item_v1> getItems() {
        return new ArrayList<>(this.items);
    }

    public void setItems(Collection<Item_v1> items) {
        this.items = new ArrayList<>(items);
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
                ", subTotal=" + subTotal +
                ", total=" + total +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", items.size=" + items.size() +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Invoice_v1) {
            Invoice_v1 that = (Invoice_v1) other;
            return Objects.equal(getInvoiceNumber(), that.getInvoiceNumber());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInvoiceNumber());
    }
}
