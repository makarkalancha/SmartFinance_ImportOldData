package com.makco.smartfinance.user_interface.constants;

import com.makco.smartfinance.user_interface.validation.ErrorEnum;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User: Makar Kalancha
 * Date: 06/04/2016
 * Time: 23:37
 */
public class DialogMessages {
    private final static Logger LOG = LogManager.getLogger(DialogMessages.class);

    public static boolean showConfirmationDialog(String title, String headerText, String contentText, String ico){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        ico = (StringUtils.isBlank(ico)) ? ApplicationConstants.MAIN_WINDOW_ICO : ico;
        alertStage.getIcons().add(new Image(ico));

        if(!StringUtils.isBlank(title)){
            alert.setTitle(title);
        }
        if(!StringUtils.isBlank(headerText)){
            alert.setHeaderText(headerText);
        }
        if(!StringUtils.isBlank(contentText)){
            alert.setContentText(contentText);
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        } else {
            return false;
        }
    }

    public static void showErrorDialog(String headerText, EnumSet<ErrorEnum> errors, String ico){
        //http://stackoverflow.com/questions/27877547/styling-a-dialog-from-javafx-openjfx-dialogs-project
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        ico = (StringUtils.isBlank(ico)) ? ApplicationConstants.MAIN_WINDOW_ICO : ico;
        alertStage.getIcons().add(new Image(ico));
        alert.setTitle("Errors");
        if(!StringUtils.isBlank(headerText)){
            alert.setHeaderText(headerText);
        }

        if(!errors.isEmpty()) {
            String message = StringUtils.join(
                    errors.stream()
                            .map(error -> error.getMessage())
                            .collect(Collectors.toCollection(ArrayList::new)).toArray(),
                    "\n"
            );
            alert.setContentText(message);
        }
        alert.showAndWait();
    }

    public static void showExceptionAlert(Throwable t){
        LOG.error(t, t);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText(t.getMessage());

//            Exception ex = new FileNotFoundException("Could not find file blabla.txt");

// Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}
