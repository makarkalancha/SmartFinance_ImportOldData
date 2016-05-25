package com.makco.smartfinance.user_interface.constants;

import com.makco.smartfinance.constants.DataBaseConstants;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mcalancea on 2016-04-05.
 */
public class UserInterfaceConstants {
    private final static Logger LOG = LogManager.getLogger(UserInterfaceConstants.class);

    public static final String IMG_RESOURCE_FOLDER = "images";

    //titles
    public static final String MAIN_WINDOW_TITLE = "Hello";
    public static final String CATEGORY_MANAGEMENT_WINDOW_TITLE = "Category Management";
    public static final String CURRENCY_WINDOW_TITLE = "Currency";
    public static final String FAMILY_MEMBER_WINDOW_TITLE = "Family Member";
    public static final String ORGANIZATION_WINDOW_TITLE = "Organization";

    //category group
    public static final String CATEGORY_GROUP_TYPE_DEBIT = "Debit";
    public static final String CATEGORY_GROUP_TYPE_CREDIT = "Credit";
    public static String convertCategoryGroupTypeFromBackendToUI(String backendText){
        if(StringUtils.isEmpty(backendText)) {
            return null;
        }else if(backendText.equals(DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT.toString())
            || backendText.equals(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.DEBIT)){
            return CATEGORY_GROUP_TYPE_DEBIT;
        } else if(backendText.equals(DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT.toString())
                || backendText.equals(DataBaseConstants.CATEGORY_GROUP_TYPE.Values.CREDIT)){
            return CATEGORY_GROUP_TYPE_CREDIT;
        }

        return null;
    }

    public static DataBaseConstants.CATEGORY_GROUP_TYPE convertCategoryGroupTypeFromUIToBackend(String uiText){
        if (StringUtils.isEmpty(uiText)) {
            return null;
        } else if (uiText.equals(CATEGORY_GROUP_TYPE_DEBIT)) {
            return DataBaseConstants.CATEGORY_GROUP_TYPE.DEBIT;
        } else if (uiText.equals(CATEGORY_GROUP_TYPE_CREDIT)) {
            return DataBaseConstants.CATEGORY_GROUP_TYPE.CREDIT;
        }

        return null;
    }


    //ico
    public static final String CALENDAR = IMG_RESOURCE_FOLDER+"/calendar_60_45.png";
    public static final String MAIN_WINDOW_ICO = IMG_RESOURCE_FOLDER+"/wallet.png";
    public static final String FAMILY_MEMBER_WINDOW_ICO = IMG_RESOURCE_FOLDER+"/wallet.png";
    public static final String PROGRESS_BAR_FORM_WINDOW_ICO = IMG_RESOURCE_FOLDER+"/wallet.png";
    public static final String PROGRESS_INDICATOR_FORM_WINDOW_ICO = IMG_RESOURCE_FOLDER+"/wallet.png";
    public static final String SAVE_ICO = IMG_RESOURCE_FOLDER+"/1460658953_floppy_disk_save_16x16.png";
    public static final String REDO_ICO = IMG_RESOURCE_FOLDER+"/1460658987_arrow-redo_16x16_w.png";
    public static final String UNDO_ICO = IMG_RESOURCE_FOLDER+"/1460658987_arrow-undo_16x16_w.png";


    //key shortcut
    public static final KeyCombination SAVE_KC = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    public static final KeyCombination QUIT_KC = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    //in text fields default is called
    public static final KeyCombination REDO_KC = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
    public static final KeyCombination UNDO_KC = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);

}
