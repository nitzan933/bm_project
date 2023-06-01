package client.gui.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.ResourceBundle;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utility.entity.Restaurant;
import utility.enums.DataType;
import utility.enums.RequestType;

// TODO: Auto-generated Javadoc
/**
 * The Class ControllerFX_SupplierScreen.
 */
public class ControllerFX_SupplierScreen implements IClientFxController, Initializable {

    /** The welcome label. */
    @FXML
    private Label welcomeLabel;
    
    /** The show income button. */
    @FXML
    private Button showIncomeBtn;

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
     * Show income report.
     *
     * @param event the event
     */
    @FXML
    void showIncomeReport(ActionEvent event) {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
    	File selectedDirectory = directoryChooser.showDialog(null);
    	String path = selectedDirectory.getAbsolutePath(); 
    	ArrayList<String> reportRequest = new ArrayList<String>();
    	reportRequest.add(RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_SUPPLIER_INCOME_REPORT.toString());
    	reportRequest.add(DataType.REPORT.toString());
    	reportRequest.add(String.valueOf(res.getRes_ID()));
    	reportRequest.add(path);
    	ClientUI.clientLogic.sendMessageToServer(reportRequest,DataType.REPORT,RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_SUPPLIER_INCOME_REPORT);
    	try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO error handling
			e.printStackTrace();
		}
    	if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE) {
    		//resultLabel.setText(ClientUI.clientLogic.getLastDataRecieved().toString());
    		return;
    	}
    	else {
    		String result = (String)ClientUI.clientLogic.getLastDataRecieved();
    		  byte[] decode = Base64.getDecoder().decode(result);
    		  String fileName = "\\IncomeReport.pdf";
    		  System.out.println(fileName + "in client side");
      		  File report = new File(path + fileName);
      		  int b;
      		  try {
				FileOutputStream output = new FileOutputStream(report);
				 output.write(decode, 0, decode.length);
				 output.close();
				 if(report.exists()) {
					Desktop desktop = Desktop.getDesktop();
		            desktop.open(report);
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
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
    	ControllerFX_ActiveOrdersScreen.res = res;
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
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/SupplierScreen.fxml"));
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
