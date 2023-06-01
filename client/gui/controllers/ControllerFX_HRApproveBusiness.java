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
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utility.entity.User;
import utility.enums.DataType;
import utility.enums.RequestType;

// TODO: Auto-generated Javadoc
/**
 * The Class ControllerFX_HRApproveBusiness.
 */
public class ControllerFX_HRApproveBusiness implements IClientFxController, Initializable {
	
	/** The next screen. */
	private IClientFxController nextScreen;

    /** The button sign out. */
    @FXML
    private Button buttonSignOut;

    /** The button go back. */
    @FXML
    private Button buttonGoBack;

    /** The approve business button. */
    @FXML
    private Button approveBusinessBtn;
    
    /** The approve users button. */
    @FXML
    private Button approveUsersBtn;
    
    /**
     * go to table of the users to approve.
     *
     * @param ActionEvent approveUsersBtn was pressed
     */
    @FXML
    void approveUsers(ActionEvent event) {
    	nextScreen = new ControllerFX_HRScreen();
    	ClientUI.historyStack.pushFxController(this);
    	nextScreen.start(ClientUI.parentWindow);

    }


    /**
     * make request to approve your business
     *
     * @param ActionEvent approveBusinessBtn was pressed
     */
    @FXML
    void approveBusiness(ActionEvent event) {
    	User user = ClientUI.clientLogic.getLoggedUser();
    	ClientUI.clientLogic.sendMessageToServer(user.getUser_ID(), DataType.HR_MANAGER,
    			RequestType.CLIENT_REQUEST_TO_SERVER_APRROVE_BUSINESS);
    	approveBusinessBtn.setDisable(true);

    }

    /**
     * Do go back.
     *
     * @param event the event
     */
    @FXML
    void doGoBack(ActionEvent event) {
    	doSignOut(event);
    }

    /**
     * Do sign out.
     *
     * @param event the event
     */
    @FXML
    void doSignOut(ActionEvent event) {
    	ClientUI.clientLogic.logOutUser();
    	ClientUI.historyStack.clearControllerHistory();
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
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/HRApproveBusinessScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
        stage.setTitle("HR Screen");
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
		User user = ClientUI.clientLogic.getLoggedUser();
    	ClientUI.clientLogic.sendMessageToServer(user.getUser_ID(), DataType.HR_MANAGER,
				RequestType.CLIENT_REQUEST_TO_SERVER_CHECK_APRROVE_BUSINESS);
    	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO error handling
				e.printStackTrace();
			}
    	int isApproved = (int) ClientUI.clientLogic.getLastDataRecieved();
    	if(isApproved > 0) {
    		approveBusinessBtn.setDisable(true);
    	}
    	if(isApproved != 2)
    		approveUsersBtn.setDisable(true);
		
	}

}
