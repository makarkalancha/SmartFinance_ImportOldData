package com.smartfinance.db.entities;

import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Version;

/**
 * Created by mcalancea on 2016-03-01.
 */
public class Ent/* implements Modifiable*/{
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name="T_CREATEDON", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @Column(name="T_CREATEDON", insertable = false, updatable = false)
    protected LocalTime createdOn;

//    @Temporal(TemporalType.TIMESTAMP)
    @Version
//    @Column(name="T_UPDATEDON")
    @Column(name="T_UPDATEDON", insertable = false, updatable = false)
    protected LocalTime updatedOn;

    public Ent(){

    }

    public LocalTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalTime updatedOn) {
        this.updatedOn = updatedOn;
    }

//    @PrePersist
//    protected void onCreate() {
////        if(this.createdOn == null){
////            this.createdOn = LocalTime.now();
////        }
////        this.updatedOn = LocalTime.now();
//
//        this.createdOn = LocalTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.updatedOn = LocalTime.now();
//    }
}
