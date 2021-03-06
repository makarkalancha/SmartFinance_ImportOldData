package com.makco.smartfinance.user_interface.constants;

import com.makco.smartfinance.constants.DataBaseConstants;
import com.makco.smartfinance.utils.BigDecimalUtils;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Makar Kalancha on 2016-04-05.
 */
public class UserInterfaceConstants {
    private final static Logger LOG = LogManager.getLogger(UserInterfaceConstants.class);


    public static final int SCALE = 2;
    public static int MAX_FRACTION_DIGITS = 6;
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();
    static {
        NUMBER_FORMAT.setMaximumFractionDigits(MAX_FRACTION_DIGITS);
    }

    public static final String FXML_PATH = "/com/makco/smartfinance/user_interface/";

    public final static String INVALID_CONTROL_BGCOLOR = "-fx-background-color: #ffc0cb;";
//    public final static String INVALID_CONTROL_CLASS_BGCOLOR = ".error {-fx-background-color: #ffc0cb;}";
    public final static String INVALID_CONTROL_CSS_CLASS = "error_control_background";

    public static final SimpleDateFormat FULL_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    public static final String USER_FRIENDLY_PATTERN = "YYYY-MM-DD";

    public static final String IMG_RESOURCE_FOLDER = "images";

    //titles
    public static final String MAIN_WINDOW_TITLE = "Hello";
    public static final String ACCOUNT_MANAGEMENT_WINDOW_TITLE = "Account Management";
    public static final String CATEGORY_MANAGEMENT_WINDOW_TITLE = "Category Management";
    public static final String CURRENCY_WINDOW_TITLE = "Currency";
    public static final String FAMILY_MEMBER_WINDOW_TITLE = "Family Member";
    public static final String ORGANIZATION_WINDOW_TITLE = "Organization";
    public static final String TAX_WINDOW_TITLE = "Tax";
    public static final String TAX_FORMULA_EDITOR_WINDOW_TITLE = "Tax Formula Editor";

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

    //category
    public static final String CATEGORY_ROOT_NODE = "All Categories";
    public final static String CATEGORY_TAB_NON_CATEGORY_BGCOLOR = "-fx-background-color: rgb(221,221,221)";

    //account group
    public static final String ACCOUNT_GROUP_TYPE_DEBIT = "Debit";
    public static final String ACCOUNT_GROUP_TYPE_CREDIT = "Credit";
    public static String convertAccountGroupTypeFromBackendToUI(String backendText){
        if(StringUtils.isEmpty(backendText)) {
            return null;
        }else if(backendText.equals(DataBaseConstants.ACCOUNT_GROUP_TYPE.DEBIT.toString())
                || backendText.equals(DataBaseConstants.ACCOUNT_GROUP_TYPE.Values.DEBIT)){
            return ACCOUNT_GROUP_TYPE_DEBIT;
        } else if(backendText.equals(DataBaseConstants.ACCOUNT_GROUP_TYPE.CREDIT.toString())
                || backendText.equals(DataBaseConstants.ACCOUNT_GROUP_TYPE.Values.CREDIT)){
            return ACCOUNT_GROUP_TYPE_CREDIT;
        }

        return null;
    }

    public static DataBaseConstants.ACCOUNT_GROUP_TYPE convertAccountGroupTypeFromUIToBackend(String uiText){
        if (StringUtils.isEmpty(uiText)) {
            return null;
        } else if (uiText.equals(ACCOUNT_GROUP_TYPE_DEBIT)) {
            return DataBaseConstants.ACCOUNT_GROUP_TYPE.DEBIT;
        } else if (uiText.equals(ACCOUNT_GROUP_TYPE_CREDIT)) {
            return DataBaseConstants.ACCOUNT_GROUP_TYPE.CREDIT;
        }

        return null;
    }

    //account
    public static final String ACCOUNT_ROOT_NODE = "All Accounts";
    public final static String ACCOUNT_TAB_NON_ACCOUNT_BGCOLOR = "-fx-background-color: rgb(221,221,221)";

    //tax
    public final static String TAX_INACTIVE_DAYS_BGCOLOR = "-fx-background-color: #ffc0cb;";
    public final static String TAX_DECIMAL_SEPARATOR = "(use %c as decimal separator)";
    public final static String TAX_FORMULA_LBL = "Click \"Formula Editor\" button to compose formula";
    public final static String TAX_DENORMALIZED_FORMULA_LBL = "Denormalized Fomula will be set after composing main formula";

    //tax formula
    public final static String TAX_FORMULA_VALID_CHARACTERS = new StringBuilder()
            .append("Characters: 0-9 + - * / ( ) ")
            .append(BigDecimalUtils.getDecimalSeparator())
            .toString();

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
