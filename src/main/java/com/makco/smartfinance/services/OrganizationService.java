package com.makco.smartfinance.services;

import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by Makar Kalancha on 2016-04-22.
 */
public interface OrganizationService {
    List<Organization> organizationList() throws Exception;
    Organization getOrganizationById(Long id) throws Exception;
    List<Organization> getOrganizationByName(String name) throws Exception;

    void removeOrganization(Long id) throws Exception;
    void saveOrUpdateOrganization(Organization organization) throws Exception;
    EnumSet<ErrorEnum> validate(Organization organization) throws Exception;
}
