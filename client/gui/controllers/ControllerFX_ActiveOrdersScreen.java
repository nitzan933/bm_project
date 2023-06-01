package client.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import client.utility.email.SendEmail;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utility.entity.Order;
import utility.entity.Restaurant;
import utility.enums.DataType;
import utility.enums.RequestType;   



// TODO: Auto-generated Javadoc
/**
 * The Class ControllerFX_ActiveOrdersScreen.
 */
public class ControllerFX_ActiveOrdersScreen implements IClientFxController, Initializable {
	
	/** The wait for approval table. */
	@FXML
    private TableView<Order> waitForApprovalTable;

    /** The in the kitchen table. */
    @FXML
    private TableView<Order> inTheKitchenTable;
    
    /** The tab pane. */
    @FXML
    private TabPane tabPane;

    /** The tab wait for approval. */
    @FXML
    private Tab tabWaitForApproval;
    
    /** The tab wait in the kitchen. */
    @FXML
    private Tab tabWaitInTheKitchen;

    /** The ready table. */
    @FXML
    private TableView<Order> readyTable;

    /** The completed table. */
    @FXML
    private TableView<Order> completedTable;
    
    /** The orderID column in wait for approval table. */
    @FXML
    private TableColumn<Order, Integer> orderIDwaitForApproval;

    /** The order time column in wait for approval table. */
    @FXML
    private TableColumn<Order, String> orderTimeWaitForApproval;

    /** The type of order column in wait for approval table. */
    @FXML
    private TableColumn<Order, String> typeOfOrderWaitForApproval;

    /** The phone column in wait for approval table. */
    @FXML
    private TableColumn<Order, String> phoneWaitForApproval;

    /** The address column in wait for approval table. */
    @FXML
    private TableColumn<Order, String> addressWaitForApproval;
    
    /** The order ID column in the kitchen table. */
    @FXML
    private TableColumn<Order, Integer> orderIDInTheKitchen;

    /** The order time column in the kitchen table. */
    @FXML
    private TableColumn<Order, String> orderTimeInTheKitchen;

    /** The type of order column in the kitchen table. */
    @FXML
    private TableColumn<Order, String> typeOfOrderInTheKitchen;

    /** The phone number column in the kitchen table. */
    @FXML
    private TableColumn<Order, String> phoneNumberInTheKitchen;

    /** The address column in the kitchen table. */
    @FXML
    private TableColumn<Order, String> addressInTheKitchen;
    
    /** The tab ready. */
    @FXML
    private Tab tabReady;

    /** The order ID column in ready table. */
    @FXML
    private TableColumn<Order, Integer> orderIDReady;

    /** The order time column in ready table. */
    @FXML
    private TableColumn<Order, String> orderTimeReady;

    /** The type of order column in ready table. */
    @FXML
    private TableColumn<Order, String> typeOfOrderReady;

    /** The phone number column in ready table. */
    @FXML
    private TableColumn<Order, String> phoneNumberReady;

    /** The address column in ready table. */
    @FXML
    private TableColumn<Order, String> addressReady;
    
    /** The tab completed. */
    @FXML
    private Tab tabCompleted;

    /** The order ID column in completed table. */
    @FXML
    private TableColumn<Order, Integer> orderIDCompleted;

    /** The order time column in completed table. */
    @FXML
    private TableColumn<Order, String> orderTimeCompleted;

    /** The type of order column in completed table. */
    @FXML
    private TableColumn<Order, String> typeOfOrderCompleted;

    /** The phone number column in completed table. */
    @FXML
    private TableColumn<Order, String> phoneNumberCompleted;

    /** The address column in completed table. */
    @FXML
    private TableColumn<Order, String> addressCompleted;
    
    /** The approve order button. */
    @FXML
    private Button approveOrderBtn;
    
    @FXML
    private Label errorMsg1;
    
    @FXML
    private Label errorMsg2;
    
    @FXML
    private Label errorMsg3;
    
    @FXML
    private Label errorMsg4;
    
    /** The current restaurant. */
    public static Restaurant res;
    
    private void sendEmail(Order order, String msg) {
         
    	 //String to = order.getUserEmail();
    	String to = "karinsh97@gmail.com";
    	
         // Sender's email ID needs to be mentioned
    	final String from = "mebite857@gmail.com";
    	final String Password = "group@#16";

 
         Properties prop = new Properties();
  		prop.put("mail.smtp.host", "smtp.gmail.com");
          prop.put("mail.smtp.port", "587");
          prop.put("mail.smtp.auth", "true");
          prop.put("mail.smtp.starttls.enable", "true"); //TLS
          

         
         Session session = Session.getInstance(prop,
                 new javax.mail.Authenticator() {
                     protected PasswordAuthentication getPasswordAuthentication() {
                         return new PasswordAuthentication(from, Password);
                     }
                 });

         try {

             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress(from));
             message.setRecipients(
                     Message.RecipientType.TO,
                     InternetAddress.parse(to)
             );
           message.setSubject("Your order's status updated!");
           message.setText(msg);


             Transport.send(message);


         } catch (MessagingException e) {
             e.printStackTrace();
         }
     }
    
   

    /**
	 * Go back to the previews page.
	 *
	 * @param ActionEvent of pressing back button
	 */
@FXML
    void goBack(ActionEvent event) {
    	ClientUI.historyStack.popFxController().start(ClientUI.parentWindow);
    }

    /**
     * Sign out from this user.
     *
     * @param ActionEvent of pressing sign out button
     */
    @FXML
    void signOut(ActionEvent event) {
    	ClientUI.clientLogic.logOutUser();
    	ClientUI.loginScreen.start(ClientUI.parentWindow);
    }
    
    private boolean checkWantedOrder(ObservableList<Order> currentData,String tableName) {
    	if(currentData.isEmpty() == true) {
    		switch(tableName) {
    		case "waitForApprovalTable":
    			errorMsg1.setText("please select order");
    			break;
    		case "inTheKitchenTable":
    			errorMsg2.setText("please select order");
    			break;
    		 case "readyTable":
    			errorMsg3.setText("please select order");
    			break;
    		 case "completedTable":
    			errorMsg4.setText("please select order");
    			break;
    		}
    		return false;
    	}
    		
    	return true;
    }
    
    /**
     * Approve order in waiting for approval table.
     *
     * @param ActionEvent pressing the approve order button
     */
    @FXML
    void approveOrderInWaitingForApprovalTable(ActionEvent event) {
//    	errorMsg.setVisible(false);
    	ObservableList<Order> currentData = waitForApprovalTable.getSelectionModel().getSelectedItems();
    	if(checkWantedOrder(currentData, "waitForApprovalTable") == false) 
    		return;
    	Order currentOrder = currentData.get(0); 
    	ArrayList<String> sendData = new ArrayList<>();
    	sendData.add(String.valueOf(currentOrder.getOrderID()));
    	sendData.add("in the kitchen");
		ClientUI.clientLogic.sendMessageToServer(sendData,
				DataType.ARRAYLIST_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_UPDATE_ORDER);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Error");
		    	alert.setHeaderText(null);
		    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
		    	alert.showAndWait();
		    	return;
			}
		waitForApprovalTable.getItems().remove(currentOrder);
		populateInKitchenTable();
		SendEmail sendEmail = new SendEmail(currentOrder.getUserEmail(), 
				"Your order approved! we will let you know when it is ready.");
		sendEmail.main(null);
		//sendEmail(currentOrder, "Your order approved! we will let you know when it is ready.");
    }

    /**
     * Cancel order in waiting for approval table.
     *
     * @param ActionEvent pressing the cancel order button
     */
    @FXML
    void cancelOrderInWaitingForApprovalTable(ActionEvent event) {
    	ObservableList<Order> currentData = waitForApprovalTable.getSelectionModel().getSelectedItems();
    	if(checkWantedOrder(currentData, "waitForApprovalTable") == false) 
    		return;
    	Order currentOrder = currentData.get(0); 
		ClientUI.clientLogic.sendMessageToServer(String.valueOf(currentOrder.getOrderID()),
				DataType.SINGLE_TEXT_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_CANCEL_ORDER);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Error");
		    	alert.setHeaderText(null);
		    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
		    	alert.showAndWait();
		    	return;
			}
		waitForApprovalTable.getItems().remove(currentOrder);
		
		SendEmail sendEmail = new SendEmail(currentOrder.getUserEmail(), 
				"We canceled your order. sorry:(");
		sendEmail.main(null);

    }

    /**
     * shows the dishes in specific order in the completed table.
     *
     * @param ActionEvent pressing button shoe dish in order
     */
    @FXML
    void completedShowDishesInOrder(ActionEvent event) {
    	ObservableList<Order> currentData = completedTable.getSelectionModel().getSelectedItems();
    	if(checkWantedOrder(currentData, "completedTable") == false) 
    		return;
    	if(currentData.isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Error");
	    	alert.setHeaderText(null);
	    	alert.setContentText("please select an order to show");
	    	alert.showAndWait();
	    	return;
    	}
    	Order currentOrder = currentData.get(0); 
    	showDishInOrder(currentOrder);
    }

    /**
     * shows the dishes in specific order in the in the kitchen table.
     *
     * @param ActionEvent pressing button shoe dish in order
     */
    @FXML
    void inThekitchenShowDishesInOrder(ActionEvent event) {
    	ObservableList<Order> currentData = inTheKitchenTable.getSelectionModel().getSelectedItems();
    	if(checkWantedOrder(currentData, "inTheKitchenTable") == false) 
    		return;
    	if(currentData.isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Error");
	    	alert.setHeaderText(null);
	    	alert.setContentText("please select an order to show");
	    	alert.showAndWait();
	    	return;
    	}
    	Order currentOrder = currentData.get(0); 
    	showDishInOrder(currentOrder);
    }

    /**
     * when the order is sent to the client (or takeAway) 
     * the order new order status is completed
     *
     * @param ActionEvent when pressed on button completed in ready table
     */
    @FXML
    void orderCompleted(ActionEvent event) {
    	ObservableList<Order> currentData = readyTable.getSelectionModel().getSelectedItems();
    	if(checkWantedOrder(currentData, "readyTable") == false) 
    		return;
    	Order currentOrder = currentData.get(0); 
   
    	ArrayList<String> sendData = new ArrayList<>();
    	sendData.add(String.valueOf(currentOrder.getOrderID()));
    	sendData.add("done");

		ClientUI.clientLogic.sendMessageToServer(sendData,
				DataType.ARRAYLIST_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_UPDATE_ORDER);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
		{
			Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Error");
	    	alert.setHeaderText(null);
	    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
	    	alert.showAndWait();
	    	return;
		}
		readyTable.getItems().remove(currentOrder);
		populateCompletedTable();
		SendEmail sendEmail = new SendEmail(currentOrder.getUserEmail(),
				"Your order is with you now! Thank you for your order");
		sendEmail.main(null);
		
    }

    /**
     * when the order is ready and waiting to be delivered
     *
     * @param ActionEvent when pressed on button ready in "in the kitchen" table
     */
    @FXML
    void orderReady(ActionEvent event) {
    	ObservableList<Order> currentData = inTheKitchenTable.getSelectionModel().getSelectedItems();
    	if(checkWantedOrder(currentData, "inTheKitchenTable") == false) 
    		return;
    	Order currentOrder = currentData.get(0); 
    	
    	ArrayList<String> sendData = new ArrayList<>();
    	sendData.add(String.valueOf(currentOrder.getOrderID()));
    	sendData.add("ready");

		ClientUI.clientLogic.sendMessageToServer(sendData,
				DataType.ARRAYLIST_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_SUPPLIER_UPDATE_ORDER);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Error");
		    	alert.setHeaderText(null);
		    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
		    	alert.showAndWait();
		    	return;
			}
		inTheKitchenTable.getItems().remove(currentOrder);
		populateReadyTable();
		SendEmail sendEmail = new SendEmail(currentOrder.getUserEmail(),
				"Your order is ready! soon it will be with you.");
		sendEmail.main(null);

    }

    /**
     * shows the dishes in specific order in the ready table.
     *
     * @param ActionEvent pressing button shoe dish in order
     */
    @FXML
    void readyShowDishesInOrder(ActionEvent event) {
    	ObservableList<Order> currentData = readyTable.getSelectionModel().getSelectedItems();
    	if(checkWantedOrder(currentData, "readyTable") == false) 
    		return;
    	if(currentData.isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Error");
	    	alert.setHeaderText(null);
	    	alert.setContentText("please select an order to show");
	    	alert.showAndWait();
	    	return;
    	}
    	Order currentOrder = currentData.get(0); 
    	showDishInOrder(currentOrder);
    }

    /**
     * shows the dishes in specific order in the waiting for approval table.
     *
     * @param ActionEvent pressing button shoe dish in order
     */
    @FXML
    void showDishesInOrderWaitForApproval(ActionEvent event) {
    	ObservableList<Order> currentData = waitForApprovalTable.getSelectionModel().getSelectedItems();
    	if(checkWantedOrder(currentData, "waitForApprovalTable") == false) 
    		return;
    	if(currentData.isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
	    	alert.setTitle("Error");
	    	alert.setHeaderText(null);
	    	alert.setContentText("please select an order to show");
	    	alert.showAndWait();
	    	return;
    	}
    	Order currentOrder = currentData.get(0); 
    	showDishInOrder(currentOrder);
    	
    }
    
    /**
     * Show dish in order.
     *
     * @param currentOrder the current order
     */
    private void showDishInOrder(Order currentOrder) {
    	IClientFxController nextScreen = new ControllerFX_ActiveOrdersShowDishesInOrderScreen();
    	ControllerFX_ActiveOrdersShowDishesInOrderScreen.order = currentOrder;
    	Stage stage = new Stage();
	    nextScreen.start(stage);
    }


	/**
	 * Start. upload the screen
	 *
	 * @param stage the stage
	 */
	@Override
	public void start(Stage stage) {
		Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/activeOrders.fxml"));
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
	 * upload all tables
	 */
	private void populateTable() {
		populateWaitingForApprovalTable();
		populateInKitchenTable();
		populateReadyTable();
		populateCompletedTable();
		
	}
	
	/**
	 * Populate completed table.
	 */
	public void populateCompletedTable() {
		completedTable.getItems().clear();
		ArrayList<String> request = new ArrayList<String>();
		request.add(res.getRes_ID());
		request.add("done");
		ClientUI.clientLogic.sendMessageToServer(request,
			DataType.ARRAYLIST_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST);
				try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Error");
		    	alert.setHeaderText(null);
		    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
		    	alert.showAndWait();
		    	return;
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.ORDERS_LIST) {
				System.out.println("Houston, we got a problem!");
				return;
			}	
		ArrayList<Order> recievedData = (ArrayList<Order>) ClientUI.clientLogic.getLastDataRecieved();
		completedTable.getItems().setAll(recievedData);
	}
	
	/**
	 * upload ready table.
	 */
	public void populateReadyTable() {
		readyTable.getItems().clear();
		ArrayList<String> request = new ArrayList<String>();
		request.add(res.getRes_ID());
		request.add("ready");
		ClientUI.clientLogic.sendMessageToServer(request,
			DataType.ARRAYLIST_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST);
				try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Error");
		    	alert.setHeaderText(null);
		    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
		    	alert.showAndWait();
		    	return;
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.ORDERS_LIST) {
				System.out.println("Houston, we got a problem!");
				return;
			}	
		ArrayList<Order> recievedData = (ArrayList<Order>) ClientUI.clientLogic.getLastDataRecieved();
		readyTable.getItems().setAll(recievedData);
	}
	
	/**
	 * upload in kitchen table.
	 */
	public void populateInKitchenTable() {
		inTheKitchenTable.getItems().clear();
		ArrayList<String> request = new ArrayList<String>();
		request.add(res.getRes_ID());
		request.add("in the kitchen");
		ClientUI.clientLogic.sendMessageToServer(request,
			DataType.ARRAYLIST_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST);
				try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Error");
		    	alert.setHeaderText(null);
		    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
		    	alert.showAndWait();
		    	return;
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.ORDERS_LIST) {
				System.out.println("Houston, we got a problem!");
				return;
			}	
		ArrayList<Order> recievedData = (ArrayList<Order>) ClientUI.clientLogic.getLastDataRecieved();
		inTheKitchenTable.getItems().setAll(recievedData);
		
	}
	
	/**
	 * upload waiting for approval table.
	 */
	public void populateWaitingForApprovalTable(){
		waitForApprovalTable.getItems().clear();
		ArrayList<String> request = new ArrayList<>();
		request.add(String.valueOf(res.getRes_ID()));
		request.add("wait");
		ClientUI.clientLogic.sendMessageToServer(request,
				DataType.ARRAYLIST_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_GET_ORDERS_BY_RESTAURANT_ID_REQUEST);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("Error");
		    	alert.setHeaderText(null);
		    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
		    	alert.showAndWait();
		    	return;
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.ORDERS_LIST) {
				System.out.println("Houston, we got a problem!");
				return;
			}	
		ArrayList<Order> recievedData = (ArrayList<Order>) ClientUI.clientLogic.getLastDataRecieved();
		waitForApprovalTable.getItems().setAll(recievedData);
	}
	
	

	/**
	 * Initialize.
	 *
	 * @param location the location
	 * @param resources the resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderIDwaitForApproval.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderID"));
		orderTimeWaitForApproval.setCellValueFactory(new PropertyValueFactory<Order, String>("timeOfOrder"));
		typeOfOrderWaitForApproval.setCellValueFactory(new PropertyValueFactory<Order, String>("typeOfOrder"));
		phoneWaitForApproval.setCellValueFactory(new PropertyValueFactory<Order, String>("userPhone"));
		addressWaitForApproval.setCellValueFactory(new PropertyValueFactory<Order, String>("deliveryAddress"));
		
		orderIDInTheKitchen.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderID"));
		orderTimeInTheKitchen.setCellValueFactory(new PropertyValueFactory<Order, String>("timeOfOrder"));
		typeOfOrderInTheKitchen.setCellValueFactory(new PropertyValueFactory<Order, String>("typeOfOrder"));
		phoneNumberInTheKitchen.setCellValueFactory(new PropertyValueFactory<Order, String>("userPhone"));
		addressInTheKitchen.setCellValueFactory(new PropertyValueFactory<Order, String>("deliveryAddress"));
		
		orderIDReady.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderID"));
		orderTimeReady.setCellValueFactory(new PropertyValueFactory<Order, String>("timeOfOrder"));
		typeOfOrderReady.setCellValueFactory(new PropertyValueFactory<Order, String>("typeOfOrder"));
		phoneNumberReady.setCellValueFactory(new PropertyValueFactory<Order, String>("userPhone"));
		addressReady.setCellValueFactory(new PropertyValueFactory<Order, String>("deliveryAddress"));
		
		orderIDCompleted.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderID"));
		orderTimeCompleted.setCellValueFactory(new PropertyValueFactory<Order, String>("timeOfOrder"));
		typeOfOrderCompleted.setCellValueFactory(new PropertyValueFactory<Order, String>("typeOfOrder"));
		phoneNumberCompleted.setCellValueFactory(new PropertyValueFactory<Order, String>("userPhone"));
		addressCompleted.setCellValueFactory(new PropertyValueFactory<Order, String>("deliveryAddress"));

		populateTable();
		
		
	}

}


