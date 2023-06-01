package server.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ServerMainWindowController {

	@FXML
	private Circle indicatorServerStatus;

	@FXML
	private TextField txtServerStatus;

	@FXML
	private TextField txtClientsConnected;

	@FXML
	private TextField inputTxtSchemaName;

	@FXML
	private TextField inputTxtUserName;

	@FXML
	private Button buttonStartServer;

    @FXML
    private Button buttonServerListening;

	@FXML
	private Button buttonStopServer;

	@FXML
	private Button buttonOpenLog;

	@FXML
	private Button buttonQuit;
	
    @FXML
    private TextField inputTxtServerPort;

    @FXML
    private CheckBox chkboxDefaultValues;

    @FXML
    private PasswordField inputTxtPassword;
    
    @FXML
    private Label labelShowMessage;
	
	@FXML
	TextArea logText = new TextArea();
	
	
	//Used to gain access to functions stored in ServeUI class
	private ServerUI serverUI; //not in use as the server instance is static
	
	//Flags
	private boolean flagLogWindowOpen = false;
	private boolean flagServerRunning = false;
	private boolean flagServerListening = false;
	private boolean flagServerUIset = false; //not in use as the server instance is static
	

	@FXML
	void doOpenLog(ActionEvent event) {
		if(flagLogWindowOpen)
			return;
    	Pane root = new Pane();
    	root.getChildren().add(logText);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Server Log");
        stage.setResizable(false);
        stage.setOnCloseRequest(
        	      e -> flagLogWindowOpen = false
        	  );
        stage.show();
        flagLogWindowOpen = true;
	}

	@FXML
	void doQuit(ActionEvent event) {
		doStopServer(null);
		System.exit(0);
	}

    @FXML
    void doServerListening(ActionEvent event) {
    	if(!flagServerRunning) {
    		setLabelShowMessageErrorMessage("Server not running, can't stop listening");
    		return;
    	}
    	if(flagServerListening) {
    		ServerUI.stopServerListening();
    		setLabelShowMessageSucceessMessage("Server stoped listening");
    		flagServerListening = false;
    		buttonServerListening.setText("Start Listening");
    		return;
    	}
		try {
			ServerUI.startServerListening();
		} catch (Exception e) {
			setLabelShowMessageErrorMessage("Error Starting Listening, Exception was caught");
			e.printStackTrace();
		}
		setLabelShowMessageSucceessMessage("Server started listening");
		flagServerListening = true;
		buttonServerListening.setText("Stop Listening");
		return;
    	
    }

    
    //This function starts the server
	@FXML
	void doStartServer(ActionEvent event) {
		if(flagServerRunning) {
			setLabelShowMessageErrorMessage("Server already running, can't start it again");
			return;
		}
		//input parsing
		String schemaName = inputTxtSchemaName.getText().trim();
		String userName = inputTxtUserName.getText().trim();
		String password = inputTxtPassword.getText().trim();
		String port = inputTxtServerPort.getText().trim();
		if(schemaName.isEmpty() || userName.isEmpty() || password.isEmpty() || port.isEmpty()) {
			setLabelShowMessageErrorMessage("Fill in all the details to Start Server");
			return;
		}
		if (!port.matches("[0-9]+")) {
			setLabelShowMessageErrorMessage("Server Port should be in range of 0 to 65535");
			return;
		}
		int portNum = Integer.valueOf(port);
		if(portNum < 0 || portNum > 65535) {
			setLabelShowMessageErrorMessage("Server Port should be in range of 0 to 65535");
			return;
		}
		//STARTING SERVER
		try {
			ServerUI.startServer(portNum, schemaName, userName, password, this);
		} catch (Exception e) {
			setLabelShowMessageErrorMessage("Error Starting Server, Exception was caught");
			e.printStackTrace();
			return;
		}
		//GUI UPDATE
		setLabelShowMessageSucceessMessage("Server was started");
		indicatorServerStatus.setFill(Color.LIME);
		txtServerStatus.setText("RUNNING");
		buttonServerListening.setText("Stop Listening");
		buttonServerListening.setDisable(false);
		inputTxtSchemaName.setDisable(true);
		inputTxtUserName.setDisable(true);
		inputTxtPassword.setDisable(true);
		inputTxtServerPort.setDisable(true);
		chkboxDefaultValues.setDisable(true);
		flagServerRunning = true;
		flagServerListening = true;
	}

	@FXML
	void doStopServer(ActionEvent event) {
		//STOP SERVER
		try {
			if(!ServerUI.stopServer()) {
				setLabelShowMessageErrorMessage("Server is not running");
				return;
			}
		} catch (Exception e) {
			setLabelShowMessageErrorMessage("Error Stopping Server, Exception was caught");
			e.printStackTrace();
		}
		//GUI update
		indicatorServerStatus.setFill(Color.RED);
		txtServerStatus.setText("STOPPED");
		txtClientsConnected.setText("0");
		setLabelShowMessageSucceessMessage("Server was stopped");
		buttonServerListening.setText("Stop Listening");
		buttonServerListening.setDisable(true);
		inputTxtSchemaName.setDisable(false);
		inputTxtUserName.setDisable(false);
		inputTxtPassword.setDisable(false);
		inputTxtServerPort.setDisable(false);
		chkboxDefaultValues.setDisable(false);
		//
		flagServerRunning = false;
		flagServerListening = false;
	}
	
    @FXML
    void doFillDefault(ActionEvent event) {
    	if(chkboxDefaultValues.isSelected()) {
    		inputTxtSchemaName.setText(ServerUI.DB_NAME);
    		inputTxtUserName.setText(ServerUI.DB_USER);
    		inputTxtPassword.setText(ServerUI.DB_PASS);
    		inputTxtServerPort.setText(String.valueOf(ServerUI.DEFAULT_PORT));
    		setLabelShowMessageSucceessMessage("Default values are set");
    	}else {
    		inputTxtSchemaName.setText("");
    		inputTxtUserName.setText("");
    		inputTxtPassword.setText("");
    		inputTxtServerPort.setText("");
    		setLabelShowMessageSucceessMessage("Default values are cleared");
    	}
    }
	
    
    public void updateDataLog(String dataToShow){
		Platform.runLater(new Runnable() {
		    @Override public void run() {
		    	logText.appendText(dataToShow+"\n");
		    }
		});
    }
    
    public void updateNumberOfClientsConnected(int numberOfClients){
		Platform.runLater(new Runnable() {
		    @Override public void run() {
		    	txtClientsConnected.setText(String.valueOf(numberOfClients));
		    }
		});
    }
    
    
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/server/gui/ServerMainWindow.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Server Control");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(e -> {
			try {
				ServerUI.stopServer();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
			System.exit(0);
		});
        primaryStage.show();
    }

	public void setServerUI(ServerUI serverUI) {
		this.serverUI = serverUI;
		flagServerUIset = true;
	}
	
	private void setLabelShowMessageErrorMessage(String msg) {
		labelShowMessage.setText(msg);
		labelShowMessage.setStyle("-fx-text-fill: red;");
	}
	
	private void setLabelShowMessageSucceessMessage(String msg) {
		labelShowMessage.setText(msg);
		labelShowMessage.setStyle("-fx-text-fill: green;");
	}
	
	@FXML
    void importUsers(ActionEvent event) {
        ServerUI.importUsers();
    }
	
}
