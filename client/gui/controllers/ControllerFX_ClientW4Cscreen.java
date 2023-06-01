package client.gui.controllers;

import java.io.IOException;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.entity.User;
import utility.enums.DataType;
import utility.enums.RequestType;

public class ControllerFX_ClientW4Cscreen implements IClientFxController {
	
	IClientFxController previous;
	
    @FXML
    private Button buttonSignOut;

    @FXML
    private Button buttonGoBack;

    @FXML
    private Button buttonContinue;

    @FXML
    private Button buttonScanQRCode;
    
    @FXML
    private TextField textBoxW4CCode;
    
    @FXML
    private Label ErrorMsg;

    @FXML
    void doContinue(ActionEvent event) {
    	String w4cText = textBoxW4CCode.getText();
    	if(!checkW4CInputText(w4cText)) {
    		return;
    	}
    	//System.out.println(ClientUI.clientLogic.getLoggedUser().toString());
    	IClientFxController nextScreen = new ControllerFX_CategoriesScreen();
    	nextScreen.start(ClientUI.parentWindow);
    }

    @FXML
    void doGoBack(ActionEvent event) {
    	ClientUI.clientLogic.logOutUser();
    	ClientUI.historyStack.popFxController().start(ClientUI.parentWindow);
    }

    @FXML
    void doScanQRCode(ActionEvent event) {
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Not Yet Implemented");
    	alert.setHeaderText(null);
    	alert.setContentText("This functionality is not implemented yet.\nSorry.");
    	alert.showAndWait();
    }

    @FXML
    void doSignOut(ActionEvent event) {
    	ClientUI.clientLogic.logOutUser();
    	ClientUI.historyStack.clearControllerHistory();
    	ClientUI.loginScreen.start(ClientUI.parentWindow);
    }

	@Override
	public void start(Stage stage) {
        Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/ClientW4Cscreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
//      scene.getStylesheets().add(getClass().getResource("/client/client_gui/LoginFxml.css").toExternalForm());
        stage.setTitle("W4C Login");
        stage.setScene(scene);
        stage.show();
	}
	
	private boolean checkW4CInputText(String w4cInput) {
		if((w4cInput.trim().isEmpty())){
			ErrorMsg.setVisible(true);
			ErrorMsg.setText("You must enter a code");
			return false;
		}
		if(!ClientUI.clientLogic.getLoggedUser().getW4c().equals(w4cInput)) {
			ErrorMsg.setVisible(true);
			ErrorMsg.setText("The code does't match your user");
			return false;
		}
		ClientUI.clientLogic.sendMessageToServer(ClientUI.clientLogic.getLoggedUser(),
				DataType.USER, RequestType.CLIENT_REQUEST_TO_SERVER_W4C_REQUEST);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.USER) {
			System.out.println("Houston, we got a problem!");
			return false;
		}
		User user = (User)ClientUI.clientLogic.getLastDataRecieved();
		ClientUI.clientLogic.getLoggedUser().setPersonalCode(user.getPersonalCode());
		ClientUI.clientLogic.getLoggedUser().setBuisnessCode(user.getBuisnessCode());
		return true;
	}

}
