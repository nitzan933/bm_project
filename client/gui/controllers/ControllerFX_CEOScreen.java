package client.gui.controllers;

import java.io.IOException;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ControllerFX_CEOScreen implements IClientFxController{

    @FXML
    private Button signoutBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button viewQuarterlyReport;

    @FXML
    private Button viewBranchesReport;
    
    @FXML
    void doGoBack(ActionEvent event) {
    	ClientUI.clientLogic.logOutUser();
    	ClientUI.historyStack.clearControllerHistory();
    	ClientUI.loginScreen.start(ClientUI.parentWindow);
    }
  
    @FXML
    void doSignOut(ActionEvent event) {
    	ClientUI.clientLogic.logOutUser();
    	ClientUI.historyStack.clearControllerHistory();
    	ClientUI.loginScreen.start(ClientUI.parentWindow);
    }
    
    @FXML
    void moveToViewBranchesReport(ActionEvent event) {
    	IClientFxController nextScreen = new ControllerFX_ReportFromBranchesCEO();
    	ClientUI.historyStack.pushFxController(this);
    	nextScreen.start(ClientUI.parentWindow);
    }

    @FXML
    void moveToViewQuarterlyReport(ActionEvent event) {
    	IClientFxController nextScreen = new ControllerFX_ViewQuarterlyReport();
    	ClientUI.historyStack.pushFxController(this);
    	nextScreen.start(ClientUI.parentWindow);
    }

	@Override
	public void start(Stage stage) {
		Parent root = null;
        try {
        	
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/CeoScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
//      scene.getStylesheets().add(getClass().getResource("/client/client_gui/LoginFxml.css").toExternalForm());
        stage.setTitle("CEO");
        stage.setScene(scene);
        stage.show();
		
	}

}