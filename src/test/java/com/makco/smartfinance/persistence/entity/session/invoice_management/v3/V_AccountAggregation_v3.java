package com.makco.smartfinance.persistence.entity.session.invoice_management.v3;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Makar Kalancha on 28 Jul 2016 at 14:51.
 */
@Entity
@Table(name = "V_ACC_AGG")
public class V_AccountAggregation_v3 implements Serializable {
    @Column(name = "ACCOUNT_GROUP_ID")
    private Long accountGroupId;

    @Column(name = "ACCOUNT_GROUP_NAME")
    private String accountGroupName;

    @Id
    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SUM_DEBIT")
    private BigDecimal sumDebit = new BigDecimal("0");

    @Column(name = "SUM_CREDIT")
    private BigDecimal sumCredit = new BigDecimal("0");

    public V_AccountAggregation_v3(){

    }

    public Long getAccountGroupId() {
        return accountGroupId;
    }

    public String getAccountGroupName() {
        return accountGroupName;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getSumCredit() {
        return sumCredit;
    }

    public BigDecimal getSumDebit() {
        return sumDebit;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof V_AccountAggregation_v3) {
            V_AccountAggregation_v3 that = (V_AccountAggregation_v3) other;
            return Objects.equal(getAccountGroupId(), that.getAccountGroupId())
                    && Objects.equal(getAccountId(), that.getAccountId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                getAccountGroupId(),
                getAccountId()
        );
    }

    @Override
    public String toString() {
        return "AccountAggregate_v3{" +
                "accountGroupId=" + accountGroupId +
                ", accountGroupName='" + accountGroupName + '\'' +
                ", accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", type='" + type + '\'' +
                ", sumDebit=" + sumDebit +
                ", sumCredit=" + sumCredit +
                '}';
    }
}
