package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Makar Kalancha on 2016-07-14.
 */
@Entity
@Table(name = "ITEM")
public class Item implements Serializable {
    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "ITEM_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_ITEM"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "ITEM_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INVOICE_ID", referencedColumnName = "ID", nullable = false)
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TAX_ID", referencedColumnName = "ID", nullable = false)
    private Tax tax;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FAMILY_MEMBER_ID", referencedColumnName = "ID", nullable = false)
    private FamilyMember familyMember;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DATEUNIT_UNITDAY", referencedColumnName = "UNITDAY", nullable = false)
    private DateUnit dateUnit;

    @Column(name = "DESCRIPTION1")
    @Size(
            min = 0,
            max = DataBaseConstants.ITEM_DESCRIPTION1_MAX_LGTH,
            message = "Description1 length is " + DataBaseConstants.ITEM_DESCRIPTION1_MAX_LGTH + " characters."
    )
    private String description1;

    @Column(name = "DESCRIPTION2")
    @Size(
            min = 0,
            max = DataBaseConstants.ITEM_DESCRIPTION2_MAX_LGTH,
            message = "Description2 length is " + DataBaseConstants.ITEM_DESCRIPTION2_MAX_LGTH + " characters."
    )
    private String description2;

    @Column(name = "COMMENT")
    @Size(
            min = 0,
            max = DataBaseConstants.ITEM_COMMENT_MAX_LGTH,
            message = "Comment length is " + DataBaseConstants.ITEM_COMMENT_MAX_LGTH + " characters."
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

    public Item(){

    }

    public Item(Invoice invoice, Category category, Tax tax, FamilyMember familyMember,
                String description1, String description2, String comment, BigDecimal subTotal, BigDecimal total){
        this.invoice = invoice;
        this.category = category;
        this.tax = tax;
        this.familyMember = familyMember;
        this.description1 = description1;
        this.description2 = description2;
        this.comment = comment;
        this.subTotal = subTotal;
        this.total = total;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    //set date unit in invoice/item triggers

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public FamilyMember getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(FamilyMember familyMember) {
        this.familyMember = familyMember;
    }

    public Long getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn.toLocalDateTime();
    }

//    @Override
//    public String toString() {
//        return "Tax{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", rate=" + rate +
//                ", formula='" + formula + '\'' +
//                ", startDate=" + startDate +
//                ", endDate=" + endDate +
//                ", childTaxes.size=" + childTaxes.size() +
//                ", createdOn=" + createdOn +
//                ", updatedOn=" + updatedOn +
//                '}';
//    }


    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", invoice=" + invoice +
                ", category=" + category +
                ", tax=" + tax +
                ", familyMember=" + familyMember +
                ", dateUnit=" + dateUnit +
                ", description1='" + description1 + '\'' +
                ", description2='" + description2 + '\'' +
                ", comment='" + comment + '\'' +
                ", subTotal=" + subTotal +
                ", total=" + total +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    @Override
    public boolean equals(Object other) {
        //todo item table get order number
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
        //todo item table get order number
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
