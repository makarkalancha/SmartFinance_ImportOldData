package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by mcalancea on 2016-04-22.
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
public class Organization implements Serializable {

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

    public Organization() {

    }

    public Organization(String name, String description) {
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
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }

        if (!(other instanceof Organization)) {
            return false;
        }

        Organization that = (Organization) other;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
        return createdOn.toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn.toLocalDateTime();
    }
}