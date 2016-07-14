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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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

    public Item(Invoice invoice, Category category, Tax tax, FamilyMember familyMember, DateUnit dateUnit,
                String description1, String description2, String comment, BigDecimal subTotal, BigDecimal total){
        this.invoice = invoice;
        this.category = category;
        this.tax = tax;
        this.familyMember = familyMember;
        this.dateUnit = dateUnit;
        this.description1 = description1;
        this.description2 = description2;
        this.comment = comment;
        this.subTotal = subTotal;
        this.total = total;
    }
}
