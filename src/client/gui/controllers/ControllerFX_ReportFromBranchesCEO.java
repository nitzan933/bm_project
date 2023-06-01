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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utility.enums.DataType;
import utility.enums.ErrorType;
import utility.enums.RequestType;

public class ControllerFX_ReportFromBranchesCEO  implements  IClientFxController, Initializable{

    @FXML
    private Button buttonSignOut;

    @FXML
    private Button buttonGoBack;

    @FXML
    private Button openBtn;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label resultLabel;


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


    @FXML
    void openRequest(ActionEvent event) {
    	ObservableList<String> item  = listView.getSelectionModel().getSelectedItems();
    	if(item.isEmpty()) {
    		resultLabel.setText("you must choose business");
    		return;
    	}
    	DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
    	File selectedDirectory = directoryChooser.showDialog(null);
    	String path = selectedDirectory.getAbsolutePath(); 
    	ArrayList<String> reportRequest = new ArrayList<String>();
    	reportRequest.add(RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_REPORT_FROM_BRANCH.toString());
    	reportRequest.add(DataType.REPORT.toString());
    	reportRequest.add(item.get(0));
    	reportRequest.add(path);
    	ClientUI.clientLogic.sendMessageToServer(reportRequest,DataType.REPORT,RequestType.CLIENT_REQUEST_TO_SERVER_OPEN_REPORT_FROM_BRANCH);
    	try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO error handling
			e.printStackTrace();
		}
    	if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE) {
    		resultLabel.setText(ClientUI.clientLogic.getLastDataRecieved().toString());
    		return;
    	}
    	else {
    		String result = (String)ClientUI.clientLogic.getLastDataRecieved();
    		  byte[] decode = Base64.getDecoder().decode(result);
    		  String[] msgArr = item.get(0).split(" ");
    		  String date = msgArr[0];
    		  String fileName = "\\" + "reportFromBranch" + "(" + date + ").pdf";
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateTable();
	}
	
	 private void populateTable() {
			ClientUI.clientLogic.sendMessageToServer("", DataType.SINGLE_TEXT_STRING,
					RequestType.CLIENT_REQUEST_TO_SERVER_CEO_GET_BRANCHES_REPORTS);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}if(!(ClientUI.clientLogic.getLastDataRecieved() instanceof ErrorType)) {	
				ArrayList<String> recievedData = (ArrayList<String>) ClientUI.clientLogic.getLastDataRecieved();
				listView.getItems().setAll(recievedData);
				if(recievedData.isEmpty()) { //if there is nothing in the list, disable the btn
					openBtn.setDisable(true);
				}else {
					openBtn.setDisable(false);
				
				}
			}else {
				listView.getItems().clear();
				openBtn.setDisable(true);
				resultLabel.setText(ClientUI.clientLogic.getLastDataRecieved().toString());
			}
		}

	@Override
	public void start(Stage stage) {
		Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/CeoReoprtFromBranches.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
        stage.setTitle("reoprts from branches");
        stage.setScene(scene);
        stage.show();
		
	}

}

