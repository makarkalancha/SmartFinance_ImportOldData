package com.makco.smartfinance.user_interface.validation.organization_rules;

import com.makco.smartfinance.persistence.entity.Organization;
import com.makco.smartfinance.services.OrganizationService;
import com.makco.smartfinance.services.OrganizationServiceImpl;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import com.makco.smartfinance.user_interface.validation.Rule;

import java.util.EnumSet;
import java.util.List;

/**
 * Created by mcalancea on 2016-04-22.
 */
public class ORG_DuplicateName implements Rule<Organization> {
    private OrganizationService organizationService = new OrganizationServiceImpl();
    @Override
    public EnumSet<ErrorEnum> validate(Organization organization) {
        EnumSet<ErrorEnum> errors = EnumSet.noneOf(ErrorEnum.class);
        try {
            List<Organization> organizations = organizationService.getOrganizationByName(organization.getName());
            organizations.removeIf(org -> org.getId().equals(organization.getId()));
            if (!organizations.isEmpty()) {
                errors.add(ErrorEnum.ORG_NAME_DUPLICATE);
            }
        }catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return errors;
    }
}
