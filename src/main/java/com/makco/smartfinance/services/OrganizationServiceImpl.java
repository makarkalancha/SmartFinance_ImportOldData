package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.dao.OrganizationDAO;
import com.makco.smartfinance.persistence.dao.OrganizationDAOImpl;
import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.RuleSet;
import com.makco.smartfinance.user_interface.validation.rule_sets.OrganizationRuleSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-22.
 */
public class OrganizationServiceImpl implements OrganizationService {
    private final static Logger LOG = LogManager.getLogger(OrganizationServiceImpl.class);
    private OrganizationDAO organizationDAO = new OrganizationDAOImpl();

    @Override
    public Organization getOrganizationById(Long id) throws Exception {
        Organization organization = null;
        try {
            organization = organizationDAO.getOrganizationById(id);
        } catch (Exception e) {
            throw e;
        }
        return organization;
    }

    @Override
    public List<Organization> organizationList() throws Exception {
        List<Organization> organizations = new ArrayList<>();
        try {
            organizations = organizationDAO.organizationList();
        } catch (Exception e) {
            throw e;
        }
        return organizations;
    }

    @Override
    public List<Organization> getOrganizationByName(String name) throws Exception {
        List<Organization> organizations = new ArrayList<>();
        try {
            organizations = organizationDAO.getOrganizationByName(name);
        } catch (Exception e) {
            throw e;
        }
        return organizations;
    }

    @Override
    public void removeOrganization(Long id) throws Exception {
        try{
            organizationDAO.removeOrganization(id);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void saveOrUpdateOrganization(Organization organization) throws Exception {
        try{
            organizationDAO.saveOrUpdateOrganization(organization);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public EnumSet<ErrorEnum> validate(Organization organization) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            RuleSet ruleSet = new OrganizationRuleSet();
            errors = ruleSet.validate(organization);
        } catch (Exception e) {
            throw e;
        }
        return errors;
    }
}
