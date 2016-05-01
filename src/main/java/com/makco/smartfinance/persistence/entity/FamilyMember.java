package com.makco.smartfinance.persistence.entity;

import com.google.common.base.Objects;
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
 * Created by mcalancea on 2016-03-01.
 */

//http://howtodoinjava.com/jpa/jpa-native-delete-sql-query-example/
@Entity
@Table(
        name="FAMILY_MEMBER",
        uniqueConstraints =
                @UniqueConstraint(
                        name="IDX_UNQ_FMLMMBR_NM",
                        columnNames = {"NAME"}
                )
)
//@org.hibernate.annotations.DynamicInsert
//@org.hibernate.annotations.DynamicUpdate
public class FamilyMember implements Serializable{
    @Id
    @org.hibernate.annotations.GenericGenerator(
            name = "FAMILY_MEMBER_SEQUENCE_GENERATOR",
            strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence_name",
                            value = "SEQ_FAMILY_MEMBER"
                    ),
                    @org.hibernate.annotations.Parameter(
                            name = "initial_value",
                            value = "1"
                    )
            }
    )
    @GeneratedValue(generator = "FAMILY_MEMBER_SEQUENCE_GENERATOR")
    @Column(name="ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    @NotNull
    @Size(
            min = 2,
            max = DataBaseConstants.FM_NAME_MAX_LGTH,
            message = "Name is required, maximum " + DataBaseConstants.FM_NAME_MAX_LGTH + " characters."
    )
    private String name;

    @Column(name = "DESCRIPTION")
    @Size(
            min = 0,
            max = DataBaseConstants.FM_DESCRIPTION_MAX_LGTH,
            message = "Description length is " + DataBaseConstants.FM_DESCRIPTION_MAX_LGTH + " characters."
    )
    private String description;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    private Timestamp createdOn;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name="T_UPDATEDON",insertable = false, updatable = false)
    private Timestamp updatedOn;

    public FamilyMember(){

    }

    public FamilyMember(String name, String description){
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

    // hibernate persistence 277 business key
    // as other can be Hibernate proxy use:
    // getters(), instanceof (instead of getClass())
    @Override
    public boolean equals(Object other) {
//        if (this == other) {
//            return true;
//        }
//        if (other == null) {
//            return false;
//        }
//
//        if (!(other instanceof FamilyMember)) {
//            return false;
//        }
//
//        FamilyMember that = (FamilyMember) other;
//
//        return getName().equals(that.getName());
        if (other instanceof FamilyMember) {
            FamilyMember that = (FamilyMember) other;
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
        return "FamilyMember{" +
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
