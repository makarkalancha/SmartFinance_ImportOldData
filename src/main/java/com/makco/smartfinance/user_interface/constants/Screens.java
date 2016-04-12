package com.makco.smartfinance.user_interface.constants;

/**
 * Created by mcalancea on 2016-04-04.
 */
public enum Screens {
    CURRENCY("currency.fxml"),
    FAMILY_MEMBER("family_member.fxml"),
    MAIN("main.fxml"),
    SCREEN1("scene1.fxml"),
    SCREEN2("scene2.fxml");

    private String fxmlFilePath;
    private Screens(String fxmlFilePath) {
        this.fxmlFilePath = fxmlFilePath;
    }

    public String getFxmlFilePath() {
        return fxmlFilePath;
    }
}
