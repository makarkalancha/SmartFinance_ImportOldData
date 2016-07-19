package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Makar Kalancha on 2016-06-08.
 * v1
 */

@Entity
@Table(
        name = "TAX",
        uniqueConstraints =
        @UniqueConstraint(
                name = "IDX_UNQ_TX_NM",
                columnNames = {"NAME"}
        )
)
public class Tax implements Serializable {
    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "TAX_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_TAX"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "TAX_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotNull
    @Size(
            min = 2,
            max = DataBaseConstants.TAX_NAME_MAX_LGTH,
            message = "Name is required, maximum " + DataBaseConstants.TAX_NAME_MAX_LGTH + " characters."
    )
    private String name;

    @Column(name = "DESCRIPTION")
    @Size(
            min = 0,
            max = DataBaseConstants.TAX_DESCRIPTION_MAX_LGTH,
            message = "Description length is " + DataBaseConstants.TAX_DESCRIPTION_MAX_LGTH + " characters."
    )
    private String description;

    @Column(name = "RATE")
    private BigDecimal rate;

    /**
     * http://interactivepython.org/runestone/static/pythonds/BasicDS/InfixPrefixandPostfixExpressions.html
     */
    @Column(name = "FORMULA")
    @Size(
            min = 0,
            max = DataBaseConstants.TAX_FORMULA_MAX_LGTH,
            message = "Formula length is " + DataBaseConstants.TAX_FORMULA_MAX_LGTH + " characters."
    )
    private String formula;

    @Column(name = "DENORMALIZED_FORMULA")
    @Size(
            min = 0,
            max = DataBaseConstants.TAX_DENORMALIZED_FORMULA_MAX_LGTH,
            message = "Denormalized Formula length is " + DataBaseConstants.TAX_DENORMALIZED_FORMULA_MAX_LGTH + " characters."
    )
    private String denormalizedFormula;

    @Column(name = "STARTDATE")
    private LocalDate startDate;

    @Column(name = "ENDDATE")
    private LocalDate endDate;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "T_CREATEDON", insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "T_UPDATEDON", insertable = false, updatable = false)
    private Timestamp updatedOn;

    //http://viralpatel.net/blogs/hibernate-self-join-annotation-mapping-many-to-many-example/
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "TAX_CHILD",
            joinColumns = {@JoinColumn(name = "TAX_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "CHILD_TAX_ID", referencedColumnName = "ID")}
    )
    private Set<Tax> childTaxes = new HashSet<>();

//    @ManyToMany(mappedBy = "childTaxes", cascade = CascadeType.ALL)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "TAX_CHILD",
            joinColumns = {@JoinColumn(name = "CHILD_TAX_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "TAX_ID", referencedColumnName = "ID")}
    )
    private Set<Tax> parentTaxes = new HashSet<>();

    public Tax() {

    }

    public Tax(String name, String description, BigDecimal rate, String formula, String denormalizedFormula,
               LocalDate startDate, LocalDate endDate, Collection<Tax> childTaxes) {
        this.name = name;
        this.description = description;
        this.rate = rate;
        this.formula = formula;
        this.denormalizedFormula = denormalizedFormula;
        this.startDate = startDate;
        this.endDate = endDate;
        if(childTaxes != null) {
            this.childTaxes = new HashSet<>(childTaxes);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    //no setId method

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
        refreshDenormalizedFormula(true);
    }

    public String getDenormalizedFormula() {
        if (!StringUtils.isBlank(formula) && StringUtils.isBlank(denormalizedFormula)) {
            refreshDenormalizedFormula(true);
        }
        return denormalizedFormula;
    }

    //impossible to set DenormalizedFormula as it is set in refreshDenormalizedFormula
//    public void setDenormalizedFormula(String denormalizedFormula) {
//        this.denormalizedFormula = denormalizedFormula;
//    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
        refreshDenormalizedFormula(false);
        refreshDenormalizedFormulaInParents();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Collection<Tax> getChildTaxes() {
        return new HashSet<>(this.childTaxes);
    }

    public void setChildTaxes(Collection<Tax> childTaxes) {
        this.childTaxes = new HashSet<>(childTaxes);
        refreshDenormalizedFormula(false);
    }

    public Set<Tax> getParentTaxes() {
        return this.parentTaxes;
    }

    public void setParentTaxes(Collection<Tax> parentTaxes) {
        this.parentTaxes = new HashSet<>(parentTaxes);
    }

    public void refreshDenormalizedFormula(boolean cleanChildSet){
        if(!StringUtils.isBlank(formula)) {
            String mathExpressionToCalculate = formula.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER, rate.toString());
            mathExpressionToCalculate = mathExpressionToCalculate.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER, rate.toString());
            Set<Tax> chidrenToIterate = new HashSet<>(childTaxes);
            for (Tax tax : chidrenToIterate) {
                String placeHolder = DataBaseConstants.getTaxChildIdPlaceholder(tax.getId());
                if (mathExpressionToCalculate.contains(placeHolder)) {
                    String rateVale = (tax.getRate() == null) ? "0" : tax.getRate().toString();
                    mathExpressionToCalculate = mathExpressionToCalculate.replace(
                            placeHolder,
                            rateVale
                    );
                } else if(cleanChildSet){
                    //if for some reason tax contains in its children tax that is not in formula - remove it
                    childTaxes.remove(tax);
                }
            }
            this.denormalizedFormula = mathExpressionToCalculate.toString();
        }
    }

    private void refreshDenormalizedFormulaInParents() {
        for(Tax parent : parentTaxes){
            parent.refreshDenormalizedFormula(false);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Tax) {
            Tax that = (Tax) other;
            return Objects.equal(getName(), that.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public String toString() {
        return "Tax{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                ", formula='" + formula + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                '}';
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn.toLocalDateTime();
    }
}
