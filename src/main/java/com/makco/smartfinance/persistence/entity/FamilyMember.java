package com.makco.smartfinance.persistence.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by mcalancea on 2016-03-01.
 */

//http://howtodoinjava.com/jpa/jpa-native-delete-sql-query-example/
//@SqlResultSetMapping(name="deleteResult",
//        columns = { @ColumnResult(name = "count")}
////        entities=@EntityResult(entityClass=Long.class)
//        )
//@NamedNativeQueries({
//        @NamedNativeQuery(
//                name = "deteleFamilyMemberNative",
//                query = "UPDATE {h-schema}FAMILY_MEMBER SET ISDELETED = true WHERE ID = ? "
////                , resultClass = FamilyMember.class //Caused by: org.h2.jdbc.JdbcSQLException: Method is only allowed for a query. Use execute or executeUpdate instead of executeQuery; SQL statement:                 UPDATE TEST.FAMILY_MEMBER SET ISDELETED = true WHERE ID = ? [90002-191]
////                , resultSetMapping = "deleteResult" //org.h2.jdbc.JdbcSQLException: Method is only allowed for a query. Use execute or executeUpdate instead of executeQuery; SQL statement:                 UPDATE TEST.FAMILY_MEMBER SET ISDELETED = true WHERE ID = ? [90002-191]
//        )
//})
//
//@org.hibernate.annotations.Loader(
//        namedQuery = "deteleFamilyMemberNative"
//)



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
//Override the default Hibernation delete and set the deleted flag rather than deleting the record from the db.
//@SQLDelete(sql="UPDATE {h-schema}FAMILY_MEMBER SET ISDELETED = true WHERE ID = ? ")
//@SQLDelete(sql="UPDATE FAMILY_MEMBER SET ISDELETED = true WHERE ID = ? ")
//@SQLDelete(sql="deteleFamilyMemberNative")
//@SQLDelete(sql="deteleFamilyMemberNative; UPDATE FAMILY_MEMBER SET ISDELETED = true WHERE ID = ? ")
//Filter added to retrieve only records that have not been soft deleted.
//impossible to select records with field isdeleted
//@Where(clause="ISDELETED <> true")
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

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.CreationTimestamp
    @Column(name="T_CREATEDON",insertable = false, updatable = false)
    protected Calendar createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @org.hibernate.annotations.UpdateTimestamp
    @Column(name="T_UPDATEDON",insertable = false, updatable = false)
    protected Calendar updatedOn;

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
            '}';
    }

    public LocalDateTime getCreatedOn() {
        return createdOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
