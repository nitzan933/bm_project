package client.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utility.entity.Dish;
import utility.entity.Restaurant;
import utility.enums.DataType;
import utility.enums.RequestType;

public class ControllerFX_MenuScreen implements IClientFxController, Initializable {

    @FXML
    private SplitPane splitPane;

    @FXML
    private Label restaurantNameLabel;

    @FXML
    private Tab firstsTab;

    @FXML
    private Tab mainDishTab;

    @FXML
    private Tab dessertsTab;

    @FXML
    private Tab drinksTab;

    @FXML
    private Button exitCartButton1;

    @FXML
    private VBox cartVBox;

    @FXML
    private GridPane cartDishesGrid;

    @FXML
    private Label labelTotalPrice;

    @FXML
    private Label emptyCartLabel1;
    
    public static Restaurant res;

    @FXML
    void exitCart(ActionEvent event) {
    	splitPane.setDividerPosition(0, 1);
    }
    
	@FXML
	void showCart(ActionEvent event) {
	    splitPane.setDividerPosition(0, 0.7);
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
	public void start(Stage stage) {
		Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/menu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String resName = res.getResName();
		restaurantNameLabel.setText(resName);
		 ClientUI.clientLogic.sendMessageToServer(res.getRes_ID(),
					DataType.SINGLE_TEXT_STRING, RequestType.CLIENT_REQUEST_TO_SERVER_MENU_REQUEST);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() == DataType.ERROR_MESSAGE)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("No menu");
		    	alert.setHeaderText(null);
		    	alert.setContentText("The menu of " + resName + " is currently empty. sorry.");
		    	alert.showAndWait();
		    	return;
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.DISHES_LIST) {
				System.out.println("Houston, we got a problem!");
				return;
			}
			
			ArrayList<Dish> DishesToShow = (ArrayList<Dish>)ClientUI.clientLogic.getLastDataRecieved();
	    	
			if(DishesToShow.size() == 0)
			{
				Alert alert = new Alert(AlertType.INFORMATION);
		    	alert.setTitle("No menu");
		    	alert.setHeaderText(null);
		    	alert.setContentText("The menu of " + resName + " is currently empty. sorry.");
		    	alert.showAndWait();
		    	return;
			}
			VBox firtsVbox = new VBox();
			firtsVbox.setSpacing(10);
			firtsVbox.setPadding(new Insets(20, 20, 20, 0));
			VBox mainVbox = new VBox();
			mainVbox.setSpacing(10);
			mainVbox.setPadding(new Insets(20, 20, 20, 0));
			VBox dessertsVbox = new VBox();
			dessertsVbox.setSpacing(10);
			dessertsVbox.setPadding(new Insets(20, 20, 20, 0));
			VBox drinksVbox = new VBox();
			drinksVbox.setSpacing(10);
			drinksVbox.setPadding(new Insets(20, 20, 20, 0));
			
			for (Dish d: DishesToShow) {
				HBox dishHbox = new HBox();
				VBox dishDitailsVbox = new VBox();
				dishHbox.setSpacing(30);
				dishHbox.getChildren().add(dishDitailsVbox);
				Button b = new Button("choose dish");
				ControllerFX_MenuScreen menu = this;
				b.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	IClientFxController nextScreen = new ControllerFX_ChooseDishScreen();
		            	ControllerFX_ChooseDishScreen.dish = d;
		            	ClientUI.historyStack.pushFxController(menu);
		            	nextScreen.start(ClientUI.parentWindow);
		            }
		        });
				b.setEffect(new DropShadow());
				b.setStyle("-fx-background-color: #ffca28; -fx-background-radius: 100px;");
				dishHbox.getChildren().add(b);
				dishDitailsVbox.setSpacing(10);
				Label dishName = new Label(d.getName());
				dishName.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
				dishDitailsVbox.getChildren().add(dishName);
				dishDitailsVbox.getChildren().add(new Label(d.getDescription()));
				switch(d.getType()) {
				case "first":
					firtsVbox.getChildren().add(dishHbox);
					firtsVbox.getChildren().add(new Separator());
					break;
				case "main":
					mainVbox.getChildren().add(dishHbox);
					mainVbox.getChildren().add(new Separator());
					break;
				case "dessert":
					dessertsVbox.getChildren().add(dishHbox);
					dessertsVbox.getChildren().add(new Separator());
					break;
				case "drink":
					drinksVbox.getChildren().add(dishHbox);
					drinksVbox.getChildren().add(new Separator());
					break;
				}
				
				}
			firstsTab.setContent(firtsVbox);
			mainDishTab.setContent(mainVbox);
			dessertsTab.setContent(dessertsVbox);
			drinksTab.setContent(drinksVbox);
			
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

