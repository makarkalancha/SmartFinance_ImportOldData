package com.makco.smartfinance.db.entities;

import com.makco.smartfinance.db.Constants;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by mcalancea on 2016-03-01.
 */
@Entity
@Table(name="FAMILY_MEMBER")
public class FamilyMember implements Serializable, Deletable{
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAMILY_MEMBER_SEQUENCE_GENERATOR")
//    @SequenceGenerator(name = "FAMILY_MEMBER_SEQUENCE_GENERATOR", sequenceName = "SEQ_FAMILY_MEMBER", allocationSize=1)
    //https://github.com/press0/hibernate-jpa-best-practices/blob/master/model/src/main/java/org/jpwh/model/package-info.java
    @GeneratedValue(generator = Constants.ID_GENERATOR)
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

    @Column(name="T_CREATEDON")
    protected LocalDateTime createdOn;

    @Column(name="T_UPDATEDON")
    protected LocalDateTime updatedOn;

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

//    @PrePersist
//    @PreUpdate
//    private void onCreate() {
//        if(this.createdOn == null){
//            this.createdOn = LocalDateTime.now();
//        }
//        this.updatedOn = LocalDateTime.now();
//    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
