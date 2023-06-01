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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utility.entity.ClientChangePermission;
import utility.entity.User;
import utility.enums.DataType;
import utility.enums.ErrorType;
import utility.enums.RequestType;

public class ControllerFX_ChangePermission implements IClientFxController, Initializable{

	ObservableList<ClientChangePermission> observableClient;
	ObservableList<ClientChangePermission> selectedClient;

	@FXML
	private Button buttonSignOut;

	@FXML
	private Button buttonGoBack;

	@FXML
	private TableColumn<ClientChangePermission,String> firstName;

	@FXML
	private TableColumn<ClientChangePermission,String> lastName;

	@FXML
	private TableColumn<ClientChangePermission,String> status;

	@FXML
	private TableColumn<ClientChangePermission,String> branch;
    @FXML
    private TableColumn<ClientChangePermission,String> id;
    
	@FXML
	private TableView<ClientChangePermission> usersTable;

	@FXML
	private Button changeBtn;

	@FXML
	private ComboBox<String> statusCmbo;
	
    @FXML
    private Label resultLabel;

	@FXML
	void doChangeStatus(ActionEvent event) {
		String newStatus = statusCmbo.getValue();
		selectedClient = usersTable.getSelectionModel().getSelectedItems();
		if(checkValidInputForBusiness(newStatus,selectedClient)) {
			ClientChangePermission selected = selectedClient.get(0);
			if(selected.getStatus().equals(newStatus)) {
				resultLabel.setText("already with this status");
				return;
			}
			selected.setStatus(newStatus);
			ClientUI.clientLogic.sendMessageToServer(selected, DataType.CHANGE_PERMISSION,
					RequestType.CLIENT_REQUEST_TO_SERVER_CHANGE_PERMISSION);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			resultLabel.setText(ClientUI.clientLogic.getLastDataRecieved().toString());
			populateTable();
		}
		
	}



	private void populateTable() {
		User user = ClientUI.clientLogic.getLoggedUser();
		ClientUI.clientLogic.sendMessageToServer(user.getPersonalBranch(), DataType.GET_DATA_OF_CLIENT,
				RequestType.CLIENT_REQUEST_TO_SERVER_GET_DATA_CLIENT_INFO);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}if(!(ClientUI.clientLogic.getLastDataRecieved() instanceof ErrorType)) {	
			changeBtn.setDisable(false);
			ArrayList<ClientChangePermission> recievedData = (ArrayList<ClientChangePermission>) ClientUI.clientLogic.getLastDataRecieved();
			usersTable.getItems().setAll(recievedData);
		}else {
			usersTable.getItems().clear();
			changeBtn.setDisable(true);
			resultLabel.setText(ClientUI.clientLogic.getLastDataRecieved().toString());
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String[] categoryNames = {"frozen","active","unregistered"};
		statusCmbo.getItems().addAll(categoryNames);
		firstName.setCellValueFactory(new PropertyValueFactory<ClientChangePermission, String>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<ClientChangePermission, String>("lastName"));
		status.setCellValueFactory(new PropertyValueFactory<ClientChangePermission, String>("status"));
		branch.setCellValueFactory(new PropertyValueFactory<ClientChangePermission, String>("branch"));
		id.setCellValueFactory(new PropertyValueFactory<ClientChangePermission, String>("id"));
		populateTable();
	}

	@Override
	public void start(Stage stage) {
		Parent root = null;
		try {

			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/ChangePermisssionBranchManager.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Scene scene = new Scene(root);
		//      scene.getStylesheets().add(getClass().getResource("/client/client_gui/LoginFxml.css").toExternalForm());
		stage.setTitle("Branch Manager");
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	void doGoBack(ActionEvent event) {
		ClientUI.historyStack.popFxController().start(ClientUI.parentWindow);
	}

	@FXML
	void doSignOut(ActionEvent event) {
		ClientUI.clientLogic.logOutUser();
		ClientUI.historyStack.clearControllerHistory();
		ClientUI.loginScreen.start(ClientUI.parentWindow);
	}

	private boolean checkValidInputForBusiness(String newStatus,ObservableList<ClientChangePermission> selectedClient) {
		if (newStatus == null) {
			resultLabel.setText("You must select new status");
			return false;
   	 	}
		if(selectedClient.isEmpty()){
			resultLabel.setText("You must select a row");
			return false;
		}
		return true;
    }

}
