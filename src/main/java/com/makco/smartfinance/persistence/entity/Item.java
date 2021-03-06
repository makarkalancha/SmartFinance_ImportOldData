package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.utils.BigDecimalUtils;

import javax.persistence.CascadeType;
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
 * Created by Makar Kalancha on 2016-07-14.
 * v3
 */
@Entity
@Table(name = "ITEM",
        uniqueConstraints =
        @UniqueConstraint(
                name = "IDX_UNQ_TM_NVCDRDRNMBR",
                columnNames = {"INVOICE_ID", "ORDER_NUMBER"}
        )
)
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

    //no Item trigger, order number is in equals/hashcode
    @Column(name = "ORDER_NUMBER", unique = true, nullable = false)
    @NotNull
    private Integer orderNumber;

/*
item cascade:
-if invoice is deleted -> items are deleted also from DB
-if item is deleted -> invoice remains in DB
with recalculated subtotal / total from trigger
 */

    /*
    -cascade = CascadeType.ALL:
    -remove returns null even if ItemDaoImpl remove contains
    Invoice invoice = item.getInvoice();
    List<Item> items = new ArrayList<>(invoice.getItems());
    items.remove(item);
    invoice.setItems(items);
    session.saveOrUpdate(invoice);

    -ItemDaoImpl saveUpdate works with this:
    Invoice invoice = item.getInvoice();
    List<Item> items = new ArrayList<>(invoice.getItems());
    invoice.setItems(items);
    >>>>session.saveOrUpdate(item);<<<< save item
     */
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
/*
-cascade = no CascadeType.ALL (at invoice level):
-remove works
Invoice invoice = item.getInvoice();
List<Item> items = new ArrayList<>(invoice.getItems());
items.remove(item);
invoice.setItems(items);
>>>session.saveOrUpdate(invoice);<<< works even without this code
session.delete(item);//it's enough to delete item

ItemDaoImpl saveUpdate works with this:
Invoice invoice = item.getInvoice();
List<Item> items = new ArrayList<>(invoice.getItems());
invoice.setItems(items);
session.saveOrUpdate(item);

-ItemDaoImpl saveUpdate works with this:
Invoice invoice = item.getInvoice();
List<Item> items = new ArrayList<>(invoice.getItems());
invoice.setItems(items);
>>>>session.saveOrUpdate(invoice);<<<< save invoice
 */
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
    @JoinColumn(name = "DATEUNIT_UNITDAY", referencedColumnName = "UNITDAY", nullable = false, insertable = false, updatable = false)
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
    private BigDecimal subTotal = new BigDecimal("0");

    @Column(name = "TOTAL")
    private BigDecimal total = new BigDecimal("0");

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "T_CREATEDON", insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "T_UPDATEDON", insertable = false, updatable = false)
    private Timestamp updatedOn;

    public Item() {

    }

    public Item(Integer orderNumber, Invoice invoice, Category category, Tax tax, FamilyMember familyMember,
                   String description1, String description2, String comment, BigDecimal subTotal)
            throws Exception{
        this.orderNumber = orderNumber;
        this.invoice = invoice;
        this.category = category;
        this.tax = tax;
        this.familyMember = familyMember;
        this.description1 = description1;
        this.description2 = description2;
        this.comment = comment;
        this.subTotal = subTotal;
        setTotal();
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

    public void setSubTotal(BigDecimal subTotal) throws Exception {
        this.subTotal = subTotal;
        setTotal();
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

    private final void setTotal() throws Exception {
        this.total = BigDecimalUtils.calculateFormulaNashorn(this.tax.getDenormalizedFormula(), this.subTotal);
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    //no Item trigger, order number is in equals/hashcode
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", orderNumber=" + orderNumber +
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
        if (other instanceof Item) {
            Item that = (Item) other;
            return Objects.equal(getOrderNumber(), that.getOrderNumber())
                    && Objects.equal(getInvoice(), that.getInvoice());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                getOrderNumber(),
                getInvoice()
        );
    }
}
