package com.makco.smartfinance.user_interface.constants;

/**
 * Created by Makar Kalancha on 2016-04-04.
 */
public enum MainScreens {
    ACCOUNT_MANAGEMENT("account_management.fxml"),
    CATEGORY_MANAGEMENT("category_management.fxml"),
    CURRENCY("currency.fxml"),
    FAMILY_MEMBER("family_member.fxml"),
    MAIN("main.fxml"),
    ORGANIZATION("organization.fxml"),
    SCREEN1("scene1.fxml"),
    SCREEN2("scene2.fxml"),
//    TAX("tax.fxml");
    TAX("tax_v1.fxml");

    private String fxmlFilePath;
    private MainScreens(String fxmlFilePath) {
        this.fxmlFilePath = fxmlFilePath;
    }

    public String getFxmlFilePath() {
        return fxmlFilePath;
    }
}
