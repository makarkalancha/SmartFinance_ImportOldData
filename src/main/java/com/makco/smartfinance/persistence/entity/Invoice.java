package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-07-13
 */
@Entity
@Table(name = "INVOICE")
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

    @ManyToOne(fetch = FetchType.EAGER)
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

    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;

    @Column(name = "TOTAL")
    private BigDecimal total;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "T_CREATEDON", insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "T_UPDATEDON", insertable = false, updatable = false)
    private Timestamp updatedOn;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    public Invoice(){

    }

    public Invoice(Organization organization, DateUnit dateUnit, String comment){
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

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    //set subtotal in item trigger

    public BigDecimal getTotal() {
        return total;
    }

    //set total in item trigger

    public LocalDateTime getCreatedOn() {
        return createdOn.toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn.toLocalDateTime();
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
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
        //todo invoice table get invoice number
//        if (other instanceof Item) {
//            Item that = (Item) other;
//            return Objects.equal(
//                    && Objects.equal(getInvoice(), that.getInvoice())
//                    && Objects.equal(getCategory(), that.getCategory())
//                    && Objects.equal(getTax(), that.getTax())
//                    && Objects.equal(getFamilyMember(), that.getFamilyMember())
//                    && Objects.equal(getDateUnit(), that.getDateUnit())
//                    && Objects.equal(getDescription1(), that.getDescription1())
//                    && Objects.equal(getDescription2(), that.getDescription2())
//                    && Objects.equal(getComment(), that.getComment())
//                    && Objects.equal(getSubTotal(), that.getSubTotal())
//                    && Objects.equal(getTotal(), that.getTotal());
//        }
        return false;
    }

    @Override
    public int hashCode() {
        //todo invoice table get invoice number
        return Objects.hashCode(
//                getId(),
//                getInvoice(),
//                getCategory(),
//                getTax(),
//                getFamilyMember(),
//                getDateUnit(),
//                getDescription1(),
//                getDescription2(),
//                getComment(),
//                getSubTotal(),
                getTotal()
        );
    }
}