package client.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utility.entity.BusinessClient;
import utility.entity.User;
import utility.enums.DataType;
import utility.enums.RequestType;

// TODO: Auto-generated Javadoc
/**
 * The Class ControllerFX_HRScreen.
 */
public class ControllerFX_HRScreen implements IClientFxController, Initializable {
	
	/** The previous. */
	IClientFxController previous;
	
	/** The observable list. */
	ObservableList<BusinessClient> observableList;
	
	/** The selected client. */
	ObservableList<BusinessClient> selectedClient;
	
    /** The tab pane. */
    @FXML
    private TabPane tabPane;
	
	/** The approved table. */
	@FXML
    private Tab approvedTable;
	
    /** The wait for approval. */
    @FXML
    private Tab waitForApproval;

    /** The remove. */
    @FXML
    private Button remove;

    /** The button sign out. */
    @FXML
    private Button buttonSignOut;

    /** The button go back. */
    @FXML
    private Button buttonGoBack;

    /** The Approve button. */
    @FXML
    private Button ApproveBtn;

    /** The Decline button. */
    @FXML
    private Button DeclineBtn;
   
    /** The Table of users to approve. */
    @FXML
    private TableView<BusinessClient> Table;
    
    /** The worker ID column in Table. */
    @FXML
    private TableColumn<BusinessClient, String> workerID;

    /** The worker name column in Table. */
    @FXML
    private TableColumn<BusinessClient, String> workerName;

    /** The worker phone column in Table. */
    @FXML
    private TableColumn<BusinessClient, String> workerPhone;

    /** The worker email column in Table. */
    @FXML
    private TableColumn<BusinessClient, String> workerEmail;
    
    /** The error message. */
    @FXML
    private Label errorMsg;
    
    /** The approved users table. */
    @FXML
    private TableView<BusinessClient> approvedUsersTable;
    
     /** The approved worker ID column in approvedUsersTable. */
     @FXML
    private TableColumn<BusinessClient, String> approvedWorkerID;

    /** The approved worker name column in approvedUsersTable. */
    @FXML
    private TableColumn<BusinessClient, String> approvedWorkerName;

    /** The approved worker phone column in approvedUsersTable. */
    @FXML
    private TableColumn<BusinessClient, String> approvedWorkerPhone;

    /** The approved worker email column in approvedUsersTable. */
    @FXML
    private TableColumn<BusinessClient, String> approvedWorkerEmail;

    /**
     * Approve specific user chosen from table.
     *
     * @param ActionEvent approve button was pressed
     */
    @FXML
    void aproveRequest(ActionEvent event) {
    	errorMsg.setVisible(false);
    	selectedClient = Table.getSelectionModel().getSelectedItems();
    	if(checkSelectedClient(selectedClient) == false)
    		return;
    	BusinessClient selected = selectedClient.get(0);
    	selected.setIsApproved(true);
    	ClientUI.clientLogic.sendMessageToServer(selected, DataType.HR_MANAGER,
				RequestType.CLIENT_REQUEST_TO_SERVER_WRITE_UPDATE_TO_DB);
    	populateTable();

    }

    /**
     * Decline specific user chosen from table.
     *
     * @param ActionEvent decline button was pressed
     */
    @FXML
    void declineRequest(ActionEvent event) {
    	errorMsg.setVisible(false);
    	selectedClient = Table.getSelectionModel().getSelectedItems();
    	if(checkSelectedClient(selectedClient) == false)
    		return;
    	BusinessClient selected = selectedClient.get(0);
    	ClientUI.clientLogic.sendMessageToServer(selected, DataType.HR_MANAGER,
				RequestType.CLIENT_REQUEST_TO_SERVER_WRITE_UPDATE_TO_DB);
    	populateTable();

    }
    
    /**
     * check that HR chose user from table
     *
     * @param selectedClient the selected client
     * @return true, if successful
     */
    private boolean checkSelectedClient(ObservableList<BusinessClient> selectedClient) {
    	if(selectedClient.isEmpty()) {
    		errorMsg.setVisible(true);
    		errorMsg.setText("please select client");
    		return false;
    	}
    	return true;
    }

    /**
     * Do go back.
     *
     * @param event the event
     */
    @FXML
    void doGoBack(ActionEvent event) {
    	ClientUI.historyStack.popFxController().start(ClientUI.parentWindow);
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
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/HrScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
//      scene.getStylesheets().add(getClass().getResource("/client/client_gui/LoginFxml.css").toExternalForm());
        stage.setTitle("HR Screen");
        stage.setScene(scene);
//      populateTable();
        stage.show();
	}
    		
	
	/**
	 * Populate table.
	 */
	private void populateTable() {
		User user = ClientUI.clientLogic.getLoggedUser();
		ClientUI.clientLogic.sendMessageToServer(user.getUser_ID(), DataType.HR_MANAGER,
				RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		ArrayList<BusinessClient> recievedData = (ArrayList<BusinessClient>) ClientUI.clientLogic.getLastDataRecieved();
		Table.getItems().setAll(recievedData);
		if(recievedData.isEmpty()) { //if there is nothing in the list, disable the btn
			ApproveBtn.setDisable(true);
			DeclineBtn.setDisable(true);
		}else {
			ApproveBtn.setDisable(false);
			DeclineBtn.setDisable(false);
		}
		
		
		ClientUI.clientLogic.sendMessageToServer(user.getUser_ID(), DataType.HR_MANAGER,
				RequestType.CLIENT_REQUEST_TO_SERVER_GET_APPROVED_BUSINESS_CLIENTS);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		recievedData = (ArrayList<BusinessClient>) ClientUI.clientLogic.getLastDataRecieved();
		approvedUsersTable.getItems().setAll(recievedData);
		if(recievedData.isEmpty()) { //if there is nothing in the list, disable the btn
			remove.setDisable(true);
		}else {
			remove.setDisable(false);
		}
	}

	/**
	 * Initialize.
	 *
	 * @param location the location
	 * @param resources the resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		workerID.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("workerID"));
    	workerName.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("workerName"));
    	workerPhone.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("workerPhone"));
    	workerEmail.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("workerEmail"));
    	
    	approvedWorkerID.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("workerID"));
    	approvedWorkerName.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("workerName"));
    	approvedWorkerPhone.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("workerPhone"));
    	approvedWorkerEmail.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("workerEmail"));
    	
    	
    	populateTable();
	}
	
	/**
	 * Removes the from approved table.
	 *
	 * @param ActionEvent remove button was pressed
	 */
	@FXML
    void removeFromApproved(ActionEvent event) {
		errorMsg.setVisible(false);
    	selectedClient = approvedUsersTable.getSelectionModel().getSelectedItems();
    	if(checkSelectedClient(selectedClient) == false)
    		return;
    	BusinessClient selected = selectedClient.get(0);
    	selected.setIsApproved(false);
    	ClientUI.clientLogic.sendMessageToServer(selected, DataType.HR_MANAGER,
				RequestType.CLIENT_REQUEST_TO_SERVER_WRITE_UPDATE_TO_DB);
    	populateTable();
    }
//	
//	@FXML
//    void loadApprovedClients(ActionEvent event) {
//				    	
//    	approvedWorkerID.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("approvedWorkerID"));
//    	approvedWorkerName.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("approvedWorkerName"));
//    	approvedWorkerPhone.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("approvedWorkerPhone"));
//    	approvedWorkerEmail.setCellValueFactory(new PropertyValueFactory<BusinessClient, String>("approvedWorkerEmail"));
		
//		tabPane.getSelectionModel().select(approvedTable);
//    	
//		User user = ClientUI.clientLogic.getLoggedUser();
//		ClientUI.clientLogic.sendMessageToServer(user.getUser_ID(), DataType.HR_MANAGER,
//				RequestType.CLIENT_REQUEST_TO_SERVER_GET_APPROVED_BUSINESS_CLIENTS);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}	
//		ArrayList<BusinessClient> recievedData = (ArrayList<BusinessClient>) ClientUI.clientLogic.getLastDataRecieved();
//		approvedUsersTable.getItems().setAll(recievedData);

//    }
	

    	
}
    	