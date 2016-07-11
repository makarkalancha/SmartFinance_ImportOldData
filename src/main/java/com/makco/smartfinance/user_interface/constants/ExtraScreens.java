package com.makco.smartfinance.user_interface.constants;

/**
 * Created by Makar Kalancha on 20 Jun 2016.
 */
public enum ExtraScreens {
    TAX_FORMULA_EDITOR("tax_formula_editor.fxml");

    private String fxmlFileName;
    private String fullFxmlFilePath;
    private ExtraScreens(String fxmlFilePath) {
        this.fxmlFileName = fxmlFilePath;
        this.fullFxmlFilePath = new StringBuilder()
                .append(UserInterfaceConstants.FXML_PATH)
                .append(fxmlFileName)
                .toString();
    }

    public String getFxmlFileName() {
        return fxmlFileName;
    }

    public String getFullFxmlFilePath() {
        return fullFxmlFilePath;
    }
}
