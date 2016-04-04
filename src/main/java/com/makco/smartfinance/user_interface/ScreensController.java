package com.makco.smartfinance.user_interface;

import com.makco.smartfinance.user_interface.constants.Screens;
import java.util.EnumMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * Created by mcalancea on 2016-04-01.
 */
public class ScreensController extends VBox {
    private final static Logger LOG = LogManager.getLogger(ScreensController.class);
    EnumMap<Screens, Node> screens = new EnumMap<>(Screens.class);

    public ScreensController(){
        super();
        setPrefWidth(400f);
        setPrefWidth(640f);
        loadMenuBar();
        LOG.debug("ScreensController.constr");
    }

    public void addScreen(final Screens screen, final Node screenNode) {
        screens.put(screen, screenNode);
        LOG.debug(String.format("ScreensController.addScreen: name=%s; Node.screen=%s", screen.toString(), screenNode.toString()));
    }

    public Node getScreen(final String name) {
        return screens.get(name);
    }

    //loads the fxml file, add teh screen to the screens collection and
    //finally injects the screenPane to the contoller
    public boolean loadMenuBar(){
        LOG.debug("ScreensController.loadMenuBar");
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("menu_bar.fxml"));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen controlledScreen = ((ControlledScreen) myLoader.getController());
            controlledScreen.setScreenParent(this);
            getChildren().add(loadScreen);
            return true;
        } catch (Exception e){
            LOG.error("!>>>ScreensController.loadScreen:",e);
            return false;
        }
    }

    //loads the fxml file, add teh screen to the screens collection and
    //finally injects the screenPane to the contoller
//    public boolean loadScreen(final String name, final String resource){
    public boolean loadScreen(final Screens screen){
        LOG.debug(String.format("ScreensController.loadScreen: name=%s; resource=%s", screen, screen.getFxmlFilePath()));
        try{
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(screen.getFxmlFilePath()));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen controlledScreen = ((ControlledScreen) myLoader.getController());
            controlledScreen.setScreenParent(this);
            addScreen(screen, loadScreen);
            return true;
        } catch (Exception e){
            LOG.error("!>>>ScreensController.loadScreen:",e);
            return false;
        }
    }

    //This method tries to display the screen with a predefined name.
    //First it makes sure the screen has been already loaded. Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    //If there isn't any screen being desplayed, the new screen is just added to the root.
    public boolean setScreen(final Screens screen) {
        LOG.debug(String.format("ScreensController.setScreen: name=%s", screen.toString()));
        LOG.debug(String.format("getChildren().size()=%d", getChildren().size()));
        if (screens.get(screen) != null) {
            if (!getChildren().isEmpty() && getChildren().size() > 1) {
                LOG.debug("getChildren() is greater than 1");
                getChildren().remove(1);
                getChildren().add(1, screens.get(screen));
            } else {
                LOG.debug("getChildren() is less or equal than 1");
                getChildren().add(screens.get(screen));
            }
            return true;
        } else {
            LOG.error(screen+" -> screen hasn't been loaded!!!");
            return false;
        }

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
            LOG.error(screen.toString() + "->screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
}
