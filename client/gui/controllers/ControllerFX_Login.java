package client.gui.controllers;
import java.io.IOException;

import client.debug.TempScreenControllerFx;
import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utility.entity.User;
import utility.enums.DataType;
import utility.enums.ErrorType;
import utility.enums.RequestType;
import utility.enums.UserType;

public class ControllerFX_Login implements IClientFxController {

    @FXML
    private TextField Useremail;

    @FXML
    private TextField Password;

    @FXML
    private Button loginbutton;
    
    @FXML
    private Label ErrorMsg;

    @FXML
    void loginButtonPressed(ActionEvent event) throws IOException {
    	String userEmail = Useremail.getText();
    	String password = Password.getText();
    	if(checkValidInput(userEmail,password)) {
    		User user = new User(-1, "", "", "", userEmail, "", UserType.USER,"", "", false,password);
    		ClientUI.clientLogic.sendMessageToServer(user, DataType.USER, RequestType.CLIENT_REQUEST_TO_SERVER_LOGIN_REQUEST);
	    	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO error handling
				e.printStackTrace();
			}
	    	if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE) {
	    		ErrorMsg.setVisible(true);
	    		String errorString = ClientUI.clientLogic.getLastDataRecieved().toString();
	    		switch(errorString) {
		    		case "INVALID_CREDENTIALS_WRONG_PASSWORD":
		    			errorString = "Wrong password. Please try again";
		    			break;
		    		case "INVALID_CREDENTIALS_USER_NOT_FOUND":
		    			errorString = "Account not found. Please try again";
		    			break;
		    		case "INVALID_CREDENTIALS_USER_ALREADY_LOGGED_IN":
		    			errorString = "You already logged in";
		    			break;
		    		case "USER_ACCOUNT_IS_FROZEN":
		    			errorString = "user acount is frozen";
	                    break;
	                case "USER_IS_UNREGISTERED":
	                	errorString = "user is unregistered";
	                    break;
		    		default:
		    			errorString = ErrorType.UNKNOWN.toString();
	    		}
	    		ErrorMsg.setText(errorString);
	    		return;
	    		}
	    	if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.USER) {
	    		//something went *very* wrong!
	    		return;
	    	}
	    	User userDataFromServer = (User)ClientUI.clientLogic.getLastDataRecieved();
	    	ClientUI.clientLogic.loginUser(userDataFromServer);
	    	openWantedWindow(userDataFromServer.getUserType());
	    	}
    }
    
    private void openWantedWindow(UserType userType) throws IOException {
    	IClientFxController nextScreen = new TempScreenControllerFx();
    	if(userType == UserType.CLIENT_PERSONAL || userType == UserType.CLIENT_BUSINESS) {
    		nextScreen = new ControllerFX_ClientW4Cscreen();
    		ClientUI.historyStack.pushFxController(this);
    		nextScreen.start(ClientUI.parentWindow);
    	}
    	if(userType == UserType.HR_MANAGER) {
    		nextScreen = new ControllerFX_HRApproveBusiness();
            ClientUI.historyStack.pushFxController(this);
            nextScreen.start(ClientUI.parentWindow);
    	}
    	if(userType == UserType.BM_BRANCH_MANAGER) {
            nextScreen = new ControllerFX_BranchManagerScreen();
            ClientUI.historyStack.pushFxController(this);
            nextScreen.start(ClientUI.parentWindow);
    	}
    	if(userType == UserType.BM_CEO) {
    		 nextScreen = new ControllerFX_CEOScreen();
             ClientUI.historyStack.pushFxController(this);
             nextScreen.start(ClientUI.parentWindow);
    	}
    	if(userType == UserType.RESTAURANT_OWNER) {
            nextScreen = new ControllerFX_SupplierScreen();
            ClientUI.historyStack.pushFxController(this);
            nextScreen.start(ClientUI.parentWindow);
        }
    		
    }
    
    private boolean checkValidInput(String userEmail, String password) {
    	 if (userEmail.trim().isEmpty()) {
    		 ErrorMsg.setVisible(true);
			ErrorMsg.setText("You must enter an Email");
			return false;
    	 }
    	 if (password.trim().isEmpty()) {
    		 ErrorMsg.setVisible(true);
    		 ErrorMsg.setText("You must enter a password");
    		 return false;
    	 }
    	 return true;
    }

	@Override
	public void start(Stage stage) {
        Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/Login.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
//      scene.getStylesheets().add(getClass().getResource("/client/client_gui/LoginFxml.css").toExternalForm());
        stage.setTitle("User Login");
        stage.setScene(scene);
        stage.show();
	}

}
