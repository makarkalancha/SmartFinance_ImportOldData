package com.makco.smartfinance.user_interface;

import com.makco.smartfinance.user_interface.constants.Screens;
import com.makco.smartfinance.user_interface.undoredo.CareTaker;
import com.makco.smartfinance.user_interface.utility_screens.DialogMessages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumMap;
import java.util.Optional;

/**
 * Created by mcalancea on 2016-04-01.
 */
//public class ScreensController extends VBox {
public class ScreensController extends BorderPane {
    private final static Logger LOG = LogManager.getLogger(ScreensController.class);
    private final EnumMap<Screens, NodeControlledScrBundle> screens = new EnumMap<>(Screens.class);
    private Screens currentScreen = Screens.MAIN;
    private final CareTaker careTaker = new CareTaker();

    private ControlledScreen menubar;
    private ControlledScreen toolbar;

    private Command toolbar_Save;
    private Command toolbar_Undo;
    private Command toolbar_Redo;

    //communitcate between controllers
    //http://stackoverflow.com/questions/14187963/passing-parameters-javafx-fxml
    //http://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html#nested_controllers

    public ScreensController(){
        super();
        setPrefHeight(USE_COMPUTED_SIZE);
        setPrefWidth(USE_COMPUTED_SIZE);
//        setStyle("-fx-background-color: #336699;");
//        setPrefWidth(400f);
//        setPrefHeight(640f);
//        setAlignment(Pos.CENTER);
        loadVbox();
//        setVgrow(getChildren().get(0), Priority.ALWAYS);
        LOG.debug("ScreensController.constr");
    }

    private void addScreen(final Screens screen, final NodeControlledScrBundle nodeControlledScrBundle) {
        screens.put(screen, nodeControlledScrBundle);
        LOG.debug(String.format("ScreensController.addScreen: name=%s; Node.screen=%s", screen.toString(), nodeControlledScrBundle.toString()));
    }

    public ControlledScreen getCurrentControlledScreen() {
        return screens.get(currentScreen).getControlledScreen();
    }

    private boolean loadVbox(){
        LOG.debug("ScreensController.loadVbox");
        try {

            VBox vbox = new VBox();
//            vbox.setStyle("-fx-background-color: red;");
//            vbox.setPadding(new Insets(10));
//            vbox.setSpacing(8);
            setTop(vbox);
            vbox.getChildren().addAll(
                    menubarLoadAndSetController(),
                    toolbarLoadAndSetController()
            );

            return true;
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
            return false;
        }
    }

    //loads the fxml file, add teh screen to the screens collection and
    //finally injects the screenPane to the contoller
    private Parent menubarLoadAndSetController(){
        LOG.debug("ScreensController.menubarLoadAndSetController");
        Parent menubarParent = null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu_bar.fxml"));
            menubarParent = (Parent) fxmlLoader.load();
            menubar = ((ControlledScreen) fxmlLoader.getController());
            menubar.setScreenPage(this);
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return menubarParent;
    }

    private Parent toolbarLoadAndSetController(){
        LOG.debug("ScreensController.toolbarLoadAndSetController");
        Parent toolbarParent = null;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tool_bar.fxml"));
            toolbarParent = (Parent) fxmlLoader.load();
            toolbar = ((ControlledScreen) fxmlLoader.getController());
            toolbar.setScreenPage(this);
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
        }
        return toolbarParent;
    }

    //loads the fxml file, add teh screen to the screens collection and
    //finally injects the screenPane to the contoller
//    public boolean loadScreen(final String name, final String resource){
    public boolean loadScreen(final Screens screen){
        LOG.debug(String.format("ScreensController.loadScreen: name=%s; resource=%s", screen, screen.getFxmlFilePath()));
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(screen.getFxmlFilePath()));
            Parent childScreenNode = (Parent) myLoader.load();
            ControlledScreen controlledScreen = ((ControlledScreen) myLoader.getController());
            controlledScreen.setScreenPage(this);

            addScreen(screen, new NodeControlledScrBundle(childScreenNode, controlledScreen));
            return true;
        } catch (Exception e){
            DialogMessages.showExceptionAlert(e);
            return false;
        }
    }

    //This method tries to display the screen with a predefined name.
    //First it makes sure the screen has been already loaded. Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    //If there isn't any screen being displayed, the new screen is just added to the root.
    public boolean setScreen(final Screens screen) {
        boolean result = false;
        LOG.debug(String.format("ScreensController.setScreen: name=%s", screen.toString()));
        LOG.debug(String.format("getChildren().size()=%d", getChildren().size()));
        NodeControlledScrBundle ncToOpen = screens.get(screen);
        NodeControlledScrBundle ncCurrent = screens.get(currentScreen);
        if (ncToOpen != null){
            if(ncCurrent == null || ncCurrent.getControlledScreen().askPermissionToClose()) {

                //is always -> BorderPane.getChildre.size:2 {getChildren().size()}
                currentScreen = screen;
                careTaker.clear();//usually in refresh method there's clear
                ncCurrent.getControlledScreen().close();

                setCenter(ncToOpen.getNode());
                ncToOpen.getControlledScreen().refresh();
                menubar.refresh();
                toolbar.refresh();
                result = true;
            }
        } else {
            DialogMessages.showExceptionAlert(new Exception(screen+" -> screen hasn't been loaded!!!"));
        }
        return result;

        /*
        Node screenToRemove;
        if(screens.get(name) !- null){ //screen loaded
            if(!getChildren().isEmpty()){ //if there is more than one screen
                getChildren().add(0, screens.get(name)); //add the screen
                screentoRemove = getChildren().get(1);
                getChildren().remove(1);
            } else {
                LOG.error(name + "-> screen hasn't been loaded!!!");
                return false;
            }
         */
    }

    //this method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(final Screens screen){
        LOG.debug(String.format("ScreensController.unloadScreen: name=%s", screen.toString()));
        if(screens.remove(screen) == null) {
            DialogMessages.showExceptionAlert(new Exception(screen.toString() + "->screen didn't exist"));
            return false;
        } else {
            return true;
        }
    }

//    public UndoRedoScreen getUndoRedoScreen() {
//        return screens.get
//    }

    //http://www.nurkiewicz.com/2013/08/optional-in-java-8-cheat-sheet.html
    private static class NodeControlledScrBundle {
        private final Optional<Node> node;
        private final Optional<ControlledScreen> controlledScreen;
        public NodeControlledScrBundle(Node node, ControlledScreen controlledScreen){
            this.node = Optional.of(node);
            this.controlledScreen = Optional.of(controlledScreen);
        }

        public Node getNode() {
            return node.get();
        }

        public ControlledScreen getControlledScreen() {
            return controlledScreen.get();
        }
    }

    public CareTaker getCareTaker() {
        return careTaker;
    }

    public Command getToolbar_Redo() {
        return toolbar_Redo;
    }

    public void setToolbar_Redo(Command toolbar_Redo) {
        this.toolbar_Redo = toolbar_Redo;
    }

    public Command getToolbar_Save() {
        return toolbar_Save;
    }

    public void setToolbar_Save(Command toolbar_Save) {
        this.toolbar_Save = toolbar_Save;
    }

    public Command getToolbar_Undo() {
        return toolbar_Undo;
    }

    public void setToolbar_Undo(Command toolbar_Undo) {
        this.toolbar_Undo = toolbar_Undo;
    }
}
