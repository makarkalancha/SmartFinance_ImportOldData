package com.makco.smartfinance.persistence.entity;

import com.makco.smartfinance.constants.DataBaseConstants;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by mcalancea on 2016-04-25.
 */
@Entity
@DiscriminatorValue("C")
public class CategoryGroupCredit extends CategoryGroup{
    @Column(name = "NAME", unique = true)
    @NotNull
    @Size(
            min = 2,
            max = DataBaseConstants.CG_NAME_MAX_LGTH,
            message = "Name is required, maximum " + DataBaseConstants.CG_NAME_MAX_LGTH + " characters."
    )
    private String name;

    @Column(name = "DESCRIPTION")
    @Size(
            min = 0,
            max = DataBaseConstants.CG_DESCRIPTION_MAX_LGTH,
            message = "Description length is " + DataBaseConstants.CG_DESCRIPTION_MAX_LGTH + " characters."
    )
    private String description;

    public CategoryGroupCredit() {
    }

    public CategoryGroupCredit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

        if (!(other instanceof CategoryGroupCredit)) {
            return false;
        }

        CategoryGroupCredit that = (CategoryGroupCredit) other;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CategoryGroupCredit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }

    @Override
    public String toStringSimple() {
        return "CategoryGroupCredit{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                '}';
    }
}
