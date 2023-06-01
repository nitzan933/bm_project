package client.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utility.entity.Restaurant;
import utility.enums.DataType;
import utility.enums.RequestType;

// TODO: Auto-generated Javadoc
/**
 * The Class ControllerFX_WelcomeSupplierScreen.
 */
public class ControllerFX_WelcomeSupplierScreen implements IClientFxController, Initializable {

    /** The welcome label. */
    @FXML
    private Label welcomeLabel;
    
    /** The restaurant. */
    private Restaurant res;

    /**
     * Edits the menu.
     *
     * @param event the event
     */
    @FXML
    void editMenu(ActionEvent event) {
    	ClientUI.historyStack.pushFxController(this);
    	IClientFxController nextScreen = new ControllerFX_EditMenuscreen();
    	ControllerFX_EditMenuscreen.res = res;
	    nextScreen.start(ClientUI.parentWindow);

    }

    /**
     * Show active orders.
     *
     * @param event the event
     */
    @FXML
    void showActiveOrders(ActionEvent event) {
    	ClientUI.historyStack.pushFxController(this);
    	IClientFxController nextScreen = new ControllerFX_ActiveOrdersScreen();
	    nextScreen.start(ClientUI.parentWindow);
    }

    /**
     * Sign out.
     *
     * @param event the event
     */
    @FXML
    void signOut(ActionEvent event) {
    	ClientUI.clientLogic.logOutUser();
    	ClientUI.loginScreen.start(ClientUI.parentWindow);
    }

	/**
	 * Start.
	 *
	 * @param stage the stage
	 */
	@Override
	public void start(Stage stage) {
		Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/openPageSupplier.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
        stage.setTitle("welcome");
        stage.setScene(scene);
        stage.show();
		
	}

	/**
	 * Initialize.
	 *
	 * @param location the location
	 * @param resources the resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		int supplierID = ClientUI.clientLogic.getLoggedUser().getUser_ID();
		ClientUI.clientLogic.sendMessageToServer(ClientUI.clientLogic.getLoggedUser(),
				DataType.USER, RequestType.CLIENT_REQUEST_TO_SERVER_GET_RESTAURANT_BY_SUPPLIER_REQUEST);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.RESTAURANT) {
			System.out.println("Houston, we got a problem!");
			return;
		}
		res = (Restaurant)ClientUI.clientLogic.getLastDataRecieved();
		welcomeLabel.setText("Hey, " + res.getResName());

	}
	
}
