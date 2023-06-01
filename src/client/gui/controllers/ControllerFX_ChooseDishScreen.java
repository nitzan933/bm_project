package client.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import utility.entity.Dish;

public class ControllerFX_ChooseDishScreen implements IClientFxController, Initializable {

    public static Dish dish;
    
    private int index;
    
    private String[] priceArray;
    
    private String price;

	@FXML
    private SplitPane splitPane;

    @FXML
    private Label dishNameLabel;

    @FXML
    private Label dishDescriptionLabel;

    @FXML
    private ComboBox<String> sizeCombo;

    @FXML
    private ComboBox<String> levelOfCookingCombo;

    @FXML
    private TextField instructionsTextBox;

    @FXML
    private Label priceLabel;

    @FXML
    private Button addToCartButton;

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
    
	@FXML
	private Button buttonCheckout;
	
    @FXML
    private Label ErrorMsg;
    
    @FXML
    private Pane dishAddedPopup;


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
    
    @FXML
    void addToCart(ActionEvent event) {
    	String dishSize = "";
    	String dishLevelOfCooking = "";
    	//check the fields:
    	if(!sizeCombo.isDisabled()) {
    		if(sizeCombo.getValue() == null) {
    			//TODO: Error
    			showError(true, "dish size not selected");
    			return;
    		}
    		dishSize = sizeCombo.getValue();
    	}
    	if(!levelOfCookingCombo.isDisabled()) {
    		if(levelOfCookingCombo.getValue() == null) {
    			showError(true, "cooking level not selected");
    			return;
    		}
    		dishLevelOfCooking = levelOfCookingCombo.getValue();
    	}
    	
    	Dish tempDish =Dish.copyOfDish(dish);
    	tempDish.setPrice(tempDish.getPriceBySize(dishSize));
    	tempDish.setSize(dishSize);
    	tempDish.setCooking_level(dishLevelOfCooking);
    	ClientUI.clientLogic.addToOrder(tempDish);
    	showError(false,null);
    	dishAddedPopup.setVisible(true);
    	PauseTransition delay = new PauseTransition(Duration.seconds(2));
    	delay.setOnFinished(e -> dishAddedPopup.setVisible(false) );
    	delay.play();
    	//refresh cart
    	updateCart();
    }
    
    @FXML
    void doCheckOut(ActionEvent event) {
		IClientFxController nextScreen = new ControllerFX_ClientCheckoutScreen();
		ClientUI.historyStack.pushFxController(this);
		nextScreen.start(ClientUI.parentWindow);
    }
    
    @FXML
    void chooseSizeInCombo(ActionEvent event) {
    	index = sizeCombo.getSelectionModel().getSelectedIndex();
    	if(index<priceArray.length)
    		price = priceArray[index];    		
    	priceLabel.setText("price: " + price + "$");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dishNameLabel.setText(dish.getName());
		dishDescriptionLabel.setText(dish.getDescription());
		priceArray = dish.getPrice().split(",");
		price = "";

		if(dish.getSize() != null) {
			String[] sizes = dish.getSize().split(",");
			sizeCombo.getItems().addAll(sizes);
		}
		else {
			sizeCombo.setDisable(true);
			price = priceArray[0];
		}
		if(dish.getCooking_level() != null) {
			String[] cookingLevel = dish.getCooking_level().split(",");
			levelOfCookingCombo.getItems().addAll(cookingLevel);
		}
		else levelOfCookingCombo.setDisable(true);
		
		priceLabel.setText("price: " + price + "$");
		updateCart();
	}

	@Override
	public void start(Stage stage) {
		Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/chooseDish.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
        stage.setTitle("Choose Dish");
        stage.setScene(scene);
        stage.show();
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
    
	void showError(boolean show, String message) {
		if(message == null || message.equals("") || !show) {
			ErrorMsg.setVisible(false);
			return;
		}
		if(show) {
			ErrorMsg.setVisible(true);
			ErrorMsg.setText(message);
			return;
		}
		ErrorMsg.setVisible(false);
	}

}
