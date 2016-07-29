package com.makco.smartfinance.persistence.dao;

import com.makco.smartfinance.persistence.entity.Organization;

import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public interface OrganizationDAO {
    List<Organization> organizationList() throws Exception;
    Organization getOrganizationById(Long id) throws Exception;
    List<Organization> getOrganizationByName(String name) throws Exception;

    void removeOrganization(Long id) throws Exception;
    void saveOrUpdateOrganization(Organization organization) throws Exception;
}
