package client.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utility.entity.Dish;
import utility.entity.Restaurant;
import utility.enums.DataType;
import utility.enums.RequestType;

public class ControllerFX_CategoriesScreen implements IClientFxController, Initializable {
	
//	ArrayList<String> greetings = new ArrayList<>();
	
    @FXML
    private Button searchButton;

    @FXML
    private Button cartButton;

    @FXML
    private Button exitCartButton;

    @FXML
    private VBox cartVBox;

    @FXML
    private GridPane cartDishesGrid;

    @FXML
    private Label labelTotalPrice;

    @FXML
    private Label emptyCartLabel1;
    
    @FXML
    private Pane mainPane;

    @FXML
    private SplitPane splitPane;
    
    @FXML
    private Label labelHelloText;


	@Override
	public void start(Stage stage) {
    	ClientUI.historyStack.pushFxController(this);
        Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/CategoriesScreen.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
//      scene.getStylesheets().add(getClass().getResource("/client/client_gui/LoginFxml.css").toExternalForm());
        stage.setTitle("Categories");
        stage.setScene(scene);
        ClientUI.historyStack.setBaseController(this);
        stage.show();
	}
	
	@FXML
    void exitCart(ActionEvent event) {
		splitPane.setDividerPosition(0, 1);

    }
	
	@FXML
    void viewCart(ActionEvent event) {
		splitPane.setDividerPosition(0, 0.7);
    }
	
	 @FXML
	void searchGui(ActionEvent event) {
		IClientFxController nextScreen = new ControllerFX_ClientSearchscreen();
		ClientUI.historyStack.pushFxController(this);
	    nextScreen.start(ClientUI.parentWindow);

	}
	 
	 private void getRestaurants(String category) {
		 ArrayList<String> CategoryBranch = new ArrayList<>();
		 CategoryBranch.add(category);
		 CategoryBranch.add(ClientUI.clientLogic.getLoggedUser().getPersonalBranch());
		 ClientUI.clientLogic.sendMessageToServer(CategoryBranch,
					DataType.ARRAYLIST_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_CATEGORY_RESTAURANT_REQUEST);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("No restaurants");
		    	alert.setHeaderText(null);
		    	alert.setContentText("we dont have " + category + " restaurants in your area. sorry.");
		    	alert.showAndWait();
		    	return;
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.RESTAURANTS_LIST) {
				return;
			}
			
			ArrayList<Restaurant> RestaurantsToShow = (ArrayList<Restaurant>)ClientUI.clientLogic.getLastDataRecieved();
	    	
			if(RestaurantsToShow.size() == 0)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("No restaurants");
		    	alert.setHeaderText(null);
		    	alert.setContentText("we dont have " + category + " restaurants in your area. sorry.");
		    	alert.showAndWait();
		    	return;
			}
			IClientFxController nextScreen = new ControllerFX_ChooseRestaurantscreen();
			ControllerFX_ChooseRestaurantscreen.category = category;
			ClientUI.historyStack.pushFxController(this);
			nextScreen.start(ClientUI.parentWindow);
			
		}
	 
	 @FXML
	 void AsianCategory(ActionEvent event) {
		 getRestaurants("asian");
	 }

	    @FXML
	    void CoffeeCategory(ActionEvent event) {
	    	getRestaurants("coffee");
	    }

	    @FXML
	    void DessertsCategory(ActionEvent event) {
	    	getRestaurants("desserts");
	    }

	    @FXML
	    void FastFoodCategory(ActionEvent event) {
	    	getRestaurants("fast food");
	    }

	    @FXML
	    void HummusCategory(ActionEvent event) {
	    	getRestaurants("hummus");
	    }

	    @FXML
	    void IndianCategory(ActionEvent event) {
	    	getRestaurants("indian");
	    }

	    @FXML
	    void ItalianCategory(ActionEvent event) {
	    	getRestaurants("italian");
	    }

	    @FXML
	    void MeatCategory(ActionEvent event) {
	    	getRestaurants("meat");
	    } 
	    
	    @FXML
	    void doSignOut(ActionEvent event) {
	    	ClientUI.clientLogic.logOutUser();
	    	ClientUI.loginScreen.start(ClientUI.parentWindow);
	    }
	    
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
	    	updateCart();
	    	labelHelloText.setText("Hello " +  ClientUI.clientLogic.getLoggedUser().getFirstName() + " " 
	    	+ ClientUI.clientLogic.getLoggedUser().getLastName() + "!");
	    }
	    
		private void updateCart() {
			if(ClientUI.clientLogic.isOrderListEmpty()) {
				return;
			}
			int cartPrice = 0;
			emptyCartLabel1.setVisible(false);
			cartVBox.setVisible(true);
			cartDishesGrid.getChildren().clear();
			//for each dish in order we update the cart to show it
			for(int i = 0; i < ClientUI.clientLogic.getOrderDishes().size(); i++) {
				Dish dish = ClientUI.clientLogic.getOrderDishes().get(i);
				Label dishName = new Label(dish.getName());
				Label dishPrice = new Label(dish.getPrice());
				cartDishesGrid.add(dishName, 0, i);
				cartDishesGrid.add(dishPrice, 1, i);
				cartPrice += Integer.parseInt(dish.getPrice());
			}
			labelTotalPrice.setText(String.valueOf(cartPrice));
		}
		
	    @FXML
	    void doCheckout(ActionEvent event) {
			IClientFxController nextScreen = new ControllerFX_ClientCheckoutScreen();
			ClientUI.historyStack.pushFxController(this);
			nextScreen.start(ClientUI.parentWindow);
	    }

}
