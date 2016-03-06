package com.makco.smartfinance.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by mcalancea on 2016-03-01.
 */
@Entity
@Table(name="FAMILY_MEMBER")
public class FamilyMember implements Serializable,Deletable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "FAMILY_MEMBER_SEQUENCE_GENERATOR")
    @SequenceGenerator(name = "FAMILY_MEMBER_SEQUENCE_GENERATOR", sequenceName = "SEQ_FAMILY_MEMBER", allocationSize=1)
    @Column(name="ID")
    private long id;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
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

    @PrePersist
    @PreUpdate
    private void onCreate() {
        if(this.createdOn == null){
            this.createdOn = LocalDateTime.now();
        }
        this.updatedOn = LocalDateTime.now();
    }

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
