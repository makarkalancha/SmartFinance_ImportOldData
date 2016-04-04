package com.makco.smartfinance.user_interface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class MainController implements Initializable, ControlledScreen {
    ScreensController myController;

    @Override
    public void setScreenParent(ScreensController screenPage) {
        myController = screenPage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO
    }
}
