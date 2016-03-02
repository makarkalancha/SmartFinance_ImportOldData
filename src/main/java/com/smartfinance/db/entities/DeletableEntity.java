package com.smartfinance.db.entities;

import javax.persistence.Column;

/**
 * Created by mcalancea on 2016-03-01.
 */
public class DeletableEntity extends Ent implements Deletable{
    @Column(name="ISDELETED")
    protected boolean isDeleted = false;

    public DeletableEntity(){

    }

    @Override
    public boolean isDeleted() {
        return this.isDeleted;
    }

    @Override
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
