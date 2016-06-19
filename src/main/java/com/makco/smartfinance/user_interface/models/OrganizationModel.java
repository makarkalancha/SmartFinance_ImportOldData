package com.makco.smartfinance.user_interface.models;

import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.services.OrganizationService;
import com.makco.smartfinance.services.OrganizationServiceImpl;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class OrganizationModel {
    private final static Logger LOG = LogManager.getLogger(OrganizationModel.class);
    private OrganizationService organizationService = new OrganizationServiceImpl();
    private ObservableList<Organization> organizations = FXCollections.observableArrayList();
    private Organization pendingOrganization;

    public OrganizationModel(){

    }

    public void refreshOrganization() throws Exception {
        try{
            if (!organizations.isEmpty()) {
                organizations.clear();
            }
            organizations = FXCollections.observableArrayList(organizationService.organizationList());
            LOG.debug("organizations.size: " + organizations.size());
        }catch (Exception e){
            throw e;
        }
    }

    public ObservableList<Organization> getOrganizations() throws Exception {
        return organizations;
    }

    public EnumSet<ErrorEnum> savePendingOrganization(String name, String description) throws Exception {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            Organization tmpOrganization;
            if (pendingOrganization != null) {
                pendingOrganization.setName(name);
                pendingOrganization.setDescription(description);
                tmpOrganization = pendingOrganization;
            } else {
                tmpOrganization = new Organization(name, description);
            }

            errors = organizationService.validate(tmpOrganization);
            if (errors.isEmpty()) {
                organizationService.saveOrUpdateOrganization(tmpOrganization);
                pendingOrganization = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            refreshOrganization();
        }
        return errors;
    }

    public void deletePendingOrganization() throws Exception {
        try{
            if (pendingOrganization != null && pendingOrganization.getId() != null) {
                organizationService.removeOrganization(pendingOrganization.getId());
                pendingOrganization = null;
            }
        } catch (Exception e) {
            throw e;
        }finally {
            refreshOrganization();
        }
    }

    public Organization getPendingOrganization() throws Exception {
        return pendingOrganization;
    }

    public void setPendingOrganizationProperty(Organization organization) throws Exception {
        pendingOrganization = organization;
    }
}
