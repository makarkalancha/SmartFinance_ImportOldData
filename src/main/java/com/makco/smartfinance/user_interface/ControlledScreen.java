package com.makco.smartfinance.user_interface;

/**
 * Created by Makar Kalancha on 2016-04-01.
 */
public interface ControlledScreen {

    void setScreenPage(ScreensController screenPage);
    void refresh();
    boolean askPermissionToClose();
    void close();
}
