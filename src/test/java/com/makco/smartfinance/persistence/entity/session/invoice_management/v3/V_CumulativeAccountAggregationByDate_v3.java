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
@Table(name = "V_CUM_ACCT_AGG_DATE")
public class V_CumulativeAccountAggregationByDate_v3 implements Serializable {
    @Id
    @Column(name = "UNITDAY")
    private Long unitday;

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

    @Column(name = "CUM_SUM_DEBIT")
    private BigDecimal cumSumDebit = new BigDecimal("0");

    @Column(name = "CUM_SUM_CREDIT")
    private BigDecimal cumSumCredit = new BigDecimal("0");

    public V_CumulativeAccountAggregationByDate_v3(){

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


    public BigDecimal getCumSumCredit() {
        return cumSumCredit;
    }

    public BigDecimal getCumSumDebit() {
        return cumSumDebit;
    }

    public String getType() {
        return type;
    }

    public Long getUnitday() {
        return unitday;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof V_CumulativeAccountAggregationByDate_v3) {
            V_CumulativeAccountAggregationByDate_v3 that = (V_CumulativeAccountAggregationByDate_v3) other;
            return Objects.equal(getUnitday(), that.getUnitday())
                    && Objects.equal(getAccountGroupId(), that.getAccountGroupId())
                    && Objects.equal(getAccountId(), that.getAccountId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                getUnitday(),
                getAccountGroupId(),
                getAccountId()
        );
    }

    @Override
    public String toString() {
        return "V_CumulativeAccountAggregationByDate_v3{" +
                "unitday=" + unitday +
                ", accountGroupId=" + accountGroupId +
                ", accountGroupName='" + accountGroupName + '\'' +
                ", accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", type='" + type + '\'' +
                ", cumSumDebit=" + cumSumDebit +
                ", cumSumCredit=" + cumSumCredit +
                '}';
    }
}
