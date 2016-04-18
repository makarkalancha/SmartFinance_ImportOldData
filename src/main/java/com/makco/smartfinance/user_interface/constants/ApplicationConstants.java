package com.makco.smartfinance.user_interface.constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class ApplicationConstants {
    private final static Logger LOG = LogManager.getLogger(ApplicationConstants.class);

    public static final String DB_SCHEMA_NAME = "FINANCE";

    public static final String IMG_RESOURCE_FOLDER = "images";

    //titles
    public static final String MAIN_WINDOW_TITLE = "Hello";
    public static final String CURRENCY_WINDOW_TITLE = "Currency";
    public static final String FAMILY_MEMBER_WINDOW_TITLE = "Family Member";

    //ico
    public static final String MAIN_WINDOW_ICO = IMG_RESOURCE_FOLDER+"/wallet.png";
    public static final String SAVE_ICO = IMG_RESOURCE_FOLDER+"/1460658953_floppy_disk_save_16x16.png";
    public static final String UNDO_ICO = IMG_RESOURCE_FOLDER+"/1460658987_arrow-undo_16x16_w.png";
    public static final String REDO_ICO = IMG_RESOURCE_FOLDER+"/1460658987_arrow-redo_16x16_w.png";
    public static final String FAMILY_MEMBER_WINDOW_ICO = IMG_RESOURCE_FOLDER+"/wallet.png";

    //key shortcut
    public static final KeyCombination SAVE_KC = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination QUIT_KC = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    //in text fields default is called
    public static final KeyCombination REDO_KC = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
    public static final KeyCombination UNDO_KC = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);

}
