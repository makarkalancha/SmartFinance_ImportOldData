package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.user_interface.constants.UserInterfaceConstants;
import com.makco.smartfinance.utils.BigDecimalUtils;
import com.makco.smartfinance.utils.notation.ReversePolishNotation;
import com.makco.smartfinance.utils.notation.ReversePolishNotation1;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by mcalancea on 2016-06-08.
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
    public static final String RATE_PLACEHOLDER = "{rate}";
    public static final String TAX_ID_PATTERN = "{id\\d+}";


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
     * todo prefix postfix formula
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
            joinColumns = {@JoinColumn(name="TAX_ID")},
            inverseJoinColumns = {@JoinColumn(name="CHILD_TAX_ID")}
    )
    @MapKeyColumn(name = "CHILD_TAX_ID")
//    @Column MapKeyColumn(name = "CHILD_TAX_ID")
    private Map<Long, Tax> childTaxes = new HashMap<>();

    @ManyToMany(mappedBy = "childTaxes")
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
        this.childTaxes = childTaxes.stream().collect(Collectors.toMap(tax -> tax.getId(), tax -> tax));
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
    }


    public String getDenormalizedFormula() {
        return denormalizedFormula;
    }

    public void setDenormalizedFormula(String denormalizedFormula) {
        this.denormalizedFormula = denormalizedFormula;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Map<Long, Tax> getChildTaxes() {
        return this.childTaxes;
    }

    public void setChildTaxes(Collection<Tax> childTaxes) {
        this.childTaxes = childTaxes.stream().collect(Collectors.toMap(tax -> tax.getId(), tax -> tax));;
    }

    public Set<Tax> getParentTaxes() {
        return this.parentTaxes;
    }

    public void setParentTaxes(Collection<Tax> parentTaxes) {
        this.parentTaxes = new HashSet<>(parentTaxes);
    }

    public BigDecimal calculateFormula(BigDecimal bigDecimal){
        BigDecimal result = new BigDecimal("0");
        String mathExpressionToCalculate = denormalizedFormula.replace(DataBaseConstants.TAX_NUMBER_PLACEHOLDER, bigDecimal.toString());
        ReversePolishNotation rpn = new ReversePolishNotation(mathExpressionToCalculate, BigDecimalUtils.getDecimalSeparator(),
                UserInterfaceConstants.SCALE);
        result = rpn.evaluateReversePolishNotation();
        return result;
    }

    public String denormalizeFormula (){
        StringBuilder result = new StringBuilder();
        String mathExpressionToCalculate = denormalizedFormula.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER, rate.toString());
        mathExpressionToCalculate = mathExpressionToCalculate.replace(DataBaseConstants.TAX_RATE_PLACEHOLDER, rate.toString());
        mathExpressionToCalculate = mathExpressionToCalculate.
        return result.toString();
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
                ", childTaxes=" + childTaxes +
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
