package client.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import utility.entity.Dish;
import utility.entity.Restaurant;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public class ControllerFX_ChooseRestaurantscreen implements IClientFxController, Initializable{

    @FXML
    private SplitPane splitPane;

    @FXML
    private Label HeadLineLabel;

    @FXML
    private Button cartButton;

    @FXML
    private Label ErrorMsg;

    @FXML
    private VBox vboxRestaurants;

    @FXML
    private Button exitCartButton1;

    @FXML
    private Label emptyCartLabel1;
    
    @FXML
    private VBox cartVBox;

    @FXML
    private GridPane cartDishesGrid;

    @FXML
    private Label labelTotalPrice;
    
    public static String category;

    @FXML
    void exitCart(ActionEvent event) {
    	splitPane.setDividerPosition(0, 1);
    }

    @FXML
    void showCart(ActionEvent event) {
    	splitPane.setDividerPosition(0, 0.7);
    }
    

	@Override
	public void start(Stage stage) {
		Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/restaurants.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
        stage.setTitle("Restaurants");
        stage.setScene(scene);
        stage.show();
        
	}
	

    @FXML
    void goBack(ActionEvent event) {
    	ClientUI.historyStack.popFxController().start(ClientUI.parentWindow);
    }

    @FXML
    void signOut(ActionEvent event) {
    	ClientUI.clientLogic.logOutUser();
    	ClientUI.loginScreen.start(ClientUI.parentWindow);
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		vboxRestaurants.getChildren().clear();
    	vboxRestaurants.setSpacing(20);
    	ArrayList<Restaurant> restaurantsToShow = (ArrayList<Restaurant>) ClientUI.clientLogic.getLastDataRecieved();
    	for (Restaurant r: restaurantsToShow) {
			HBox hboxRestaurant = new HBox();
			hboxRestaurant.setSpacing(20);
			vboxRestaurants.getChildren().add(hboxRestaurant);
			hboxRestaurant.getChildren().add(new Label(r.getResName()));
			Button b = new Button("menu");
			ControllerFX_ChooseRestaurantscreen controller = this;
			b.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	IClientFxController nextScreen = new ControllerFX_MenuScreen();
	            	ControllerFX_MenuScreen.res = r;
	            	//ClientUI.historyStack.pushFxController(controller);
	            	nextScreen.start(ClientUI.parentWindow);
	            }
	        });
			b.setEffect(new DropShadow());
			b.setStyle("-fx-background-color: #ffca28; -fx-background-radius: 100px;");
			hboxRestaurant.getChildren().add(b);
    	}
    	HeadLineLabel.setText(category);
    	
    	updateCart();
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
	

}

