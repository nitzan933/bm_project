package client.gui.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utility.enums.DataType;
import utility.enums.RequestType;

public class ControllerFX_ViewQuarterlyReport implements Initializable, IClientFxController {

    @FXML
    private Button signoutBtn;

    @FXML
    private Button backBtn;

    @FXML
    private ComboBox<String> quarter1Combo;

    @FXML
    private ComboBox<String> year1Conbo;

    @FXML
    private ComboBox<String> quarter2Combo;

    @FXML
    private ComboBox<String> year2Combo;

    @FXML
    private CheckBox compareCheckBox;

    @FXML
    private Button openBtn;

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
    void compareCheckBox(ActionEvent event) {
    	if(compareCheckBox.isSelected()) {
    		quarter2Combo.setDisable(false);
        	year2Combo.setDisable(false);
    	}else {
    		quarter2Combo.setDisable(true);
        	year2Combo.setDisable(true);
    	}
    }

    @FXML
    void openReport(ActionEvent event) {
    	if(compareCheckBox.isSelected()) {
    		if(checkValidInputForTwoCEOReport(quarter1Combo.getValue(),year1Conbo.getValue(),quarter2Combo.getValue(),year2Combo.getValue())) {
    			DirectoryChooser directoryChooser = new DirectoryChooser();
    	        directoryChooser.setInitialDirectory(new File("src"));
    	    	File selectedDirectory = directoryChooser.showDialog(null);
    	    	String path = selectedDirectory.getAbsolutePath(); 
    			handleOpenReportRequest(quarter1Combo.getValue(),year1Conbo.getValue(),path);
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			handleOpenReportRequest(quarter2Combo.getValue(),year2Combo.getValue(),path);
    		}else {
    			return;
    		}
    	}else {
    		if(checkValidInputForOneCEOReport(quarter1Combo.getValue(),year1Conbo.getValue())) {
    			DirectoryChooser directoryChooser = new DirectoryChooser();
    	        directoryChooser.setInitialDirectory(new File("src"));
    	    	File selectedDirectory = directoryChooser.showDialog(null);
    	    	String path = selectedDirectory.getAbsolutePath(); 
    			handleOpenReportRequest(quarter1Combo.getValue(),year1Conbo.getValue(),path);
    		}else {
    			return;
    		}
    	}
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE) {
    		resultLabel.setText(ClientUI.clientLogic.getLastDataRecieved().toString());
    		return;
    	}else {
    		resultLabel.setText("");
			return;
    	}
    }
    
    private void handleOpenReportRequest(String quarter,String year,String path) {
    	ArrayList<String> reportRequest = new ArrayList<String>();
    	reportRequest.add(RequestType.CLIENT_REQUEST_TO_SERVER_CEO_QUARTERLY_REPORT.toString());
    	reportRequest.add(DataType.REPORT.toString());
    	reportRequest.add(quarter);
    	reportRequest.add(year);
    	reportRequest.add(path);
    	ClientUI.clientLogic.sendMessageToServer(reportRequest,DataType.REPORT,RequestType.CLIENT_REQUEST_TO_SERVER_CEO_QUARTERLY_REPORT);
    	String result = (String)ClientUI.clientLogic.getLastDataRecieved();
		  byte[] decode = Base64.getDecoder().decode(result);
		  String date = year + "-" + "quarter " + quarter;
		  String fileName = "\\QuarterlyReport-(" + date + ").pdf";
		  File report = new File(path + fileName);
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

	@Override
	public void start(Stage stage) {
		Parent root = null;
        try {
        	
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/CEOQuarterlyReport.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
//      scene.getStylesheets().add(getClass().getResource("/client/client_gui/LoginFxml.css").toExternalForm());
        stage.setTitle("Quarterly Report");
        stage.setScene(scene);
        stage.show();
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String[] quarters = {"1","2","3","4"};
		Calendar cal = Calendar.getInstance();
		int year = (cal.get(Calendar.YEAR));
		String[] years = {String.valueOf(year),String.valueOf(year-1),String.valueOf(year-2)};
		quarter1Combo.getItems().addAll(quarters);
		quarter2Combo.getItems().addAll(quarters);
		year1Conbo.getItems().addAll(years);
		year2Combo.getItems().addAll(years);
	}
	
	private boolean checkValidInputForOneCEOReport(String quarter,String year1) {
		if (quarter == null) {
			resultLabel.setText("You must choose a quarter");
			return false;
   	 	}
		if(year1 == null) {
			resultLabel.setText("You must choose a year");
			return false;
		}
		return true;
    }
	
	private boolean checkValidInputForTwoCEOReport(String quarter,String year1,String quarter2,String year2) {
		if (quarter == null) {
			resultLabel.setText("You must choose a quarter");
			return false;
   	 	}
		if(year1 == null) {
			resultLabel.setText("You must choose a year");
			return false;
		}
		
		if (quarter2 == null) {
			resultLabel.setText("You must choose a quarter for second report");
			return false;
   	 	}
		if(year2 == null) {
			resultLabel.setText("You must choose a year for second report");
			return false;
		}
		
		return true;
    }
    
    
}