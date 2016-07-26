package com.makco.smartfinance.persistence.entity.session.invoice_management.v3;

import com.google.common.base.Objects;
import com.makco.smartfinance.constants.DataBaseConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Makar Kalancha on 2016-07-18.
 * v1
 * like com.makco.smartfinance.persistence.entity.Organization but with
 * private Set<Invoice_v1> invoices = new LinkedHashSet<>();
 * and not
 * private Set<Invoice> invoices = new LinkedHashSet<>();
 */

@Entity
@Table(
        name = "ORGANIZATION",
        uniqueConstraints =
        @UniqueConstraint(
                name = "IDX_UNQ_ORGNZTN_NM",
                columnNames = {"NAME"}
        )
)
public class Organization_v3 implements Serializable {

    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "ORGANIZATION_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_ORGANIZATION"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "ORGANIZATION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotNull
    @Size(
            min = 2,
            max = DataBaseConstants.ORG_NAME_MAX_LGTH,
            message = "Name is required, maximum " + DataBaseConstants.ORG_NAME_MAX_LGTH + " characters."
    )
    private String name;

    @Column(name = "DESCRIPTION")
    @Size(
            min = 0,
            max = DataBaseConstants.ORG_DESCRIPTION_MAX_LGTH,
            message = "Description length is " + DataBaseConstants.ORG_DESCRIPTION_MAX_LGTH + " characters."
    )
    private String description;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "T_CREATEDON", insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "T_UPDATEDON", insertable = false, updatable = false)
    private Timestamp updatedOn;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    @javax.persistence.OrderBy("DATEUNIT_UNITDAY")
    private Set<Invoice_v3> invoices = new LinkedHashSet<>();

    public Organization_v3() {

    }

    public Organization_v3(String name, String description) {
        this.name = name;
        this.description = description;
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

    @Override
    public boolean equals(Object other) {
        if (other instanceof Organization_v3) {
            Organization_v3 that = (Organization_v3) other;
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
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
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
}
