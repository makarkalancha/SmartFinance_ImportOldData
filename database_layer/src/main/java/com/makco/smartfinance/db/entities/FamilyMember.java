package com.makco.smartfinance.db.entities;

import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by mcalancea on 2016-03-01.
 */

//@NamedQueries({
//        @NamedQuery(
//                name = "deteleFamilyMember",
//                query = "UPDATE FamilyMember SET isDeleted = true WHERE ID = :id"
//        )
//})

//@NamedNativeQueries({
//        @NamedNativeQuery(
//                name = "deteleFamilyMemberNative",
//                query = "UPDATE FAMILY_MEMBER SET ISDELETED = true WHERE ID = ? "
//        )
//})

@Entity
@Table(name="FAMILY_MEMBER")
//@org.hibernate.annotations.DynamicInsert
//@org.hibernate.annotations.DynamicUpdate
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
@SQLDelete(sql="UPDATE {h-schema}FAMILY_MEMBER SET ISDELETED = true WHERE ID = ? ")
//@SQLDelete(sql="deteleFamilyMember")
//Filter added to retrieve only records that have not been soft deleted.
//impossible to select records with field isdeleted
@Where(clause="ISDELETED <> true")
public class FamilyMember implements Serializable, Deletable{
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

    @Column(name="NAME")
    @NotNull
    @Size(
        min = 2,
        max = 65,
        message = "Name is required, maximum 65 characters."
    )
    private String name;

    @Column(name="DESCRIPTION")
    @Size(
            min = 0,
            max = 1024,
            message = "Description length is 1024 characters."
    )
    private String description;

    @Column(name="ISDELETED")
    protected boolean isDeleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.Generated(
            org.hibernate.annotations.GenerationTime.INSERT
    )
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    protected Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.Generated(
            org.hibernate.annotations.GenerationTime.ALWAYS
    )
    @Column(name="T_UPDATEDON",insertable = false, updatable = false)
    protected Date updatedOn;

    public FamilyMember(){

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FamilyMember that = (FamilyMember) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "FamilyMember{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                '}';
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }
}
