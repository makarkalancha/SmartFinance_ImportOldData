package com.smartfinance.entities;

import java.sql.Timestamp;
import javax.persistence.Column;

/**
 * Created by mcalancea on 2016-03-01.
 */
public abstract class Ent {
    @Column(name="T_CREATEDON")
    protected Timestamp createdOn;

    @Column(name="T_UPDATEDON")
    protected Timestamp updatedOn;

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Timestamp updatedOn) {
        this.updatedOn = updatedOn;
    }
}
