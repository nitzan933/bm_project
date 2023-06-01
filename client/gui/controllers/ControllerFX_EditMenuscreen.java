package client.gui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.gui.logic.ClientUI;
import client.interfaces.IClientFxController;
import javafx.stage.Stage;
import utility.entity.Dish;
import utility.entity.Restaurant;
import utility.enums.DataType;
import utility.enums.RequestType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
 * this screen is show to the supplier after clicking "edit menu" button in his home page
 * the screen present a list of all the supplier's dishes, 
 * the supplier can add a new dish to his menu
 * the supplier can edit an existing dish in his menu by clicking the dish's "edit dish" button
 */

public class ControllerFX_EditMenuscreen implements IClientFxController, Initializable {
	

    @FXML
    private Tab firstsTab;

    @FXML
    private Tab mainDishTab;

    @FXML
    private Tab dessertsTab;

    @FXML
    private Tab drinksTab;
    
    public static Restaurant res;
    
    public static ArrayList<Dish> menu;

    @FXML
    void addNewDish(ActionEvent event) {
    	ClientUI.historyStack.pushFxController(this);
    	IClientFxController nextScreen = new ControllerFX_AddDishScreen();
    	ControllerFX_AddDishScreen.res = res;
    	ControllerFX_AddDishScreen.menu = menu;    	
	    nextScreen.start(ClientUI.parentWindow);

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
			root = FXMLLoader.load(getClass().getResource("/client/gui/fxml/editMenu.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
        stage.setTitle("Edit your menu");
        stage.setScene(scene);
        stage.show();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		    	alert.setTitle("Error");
		    	alert.setHeaderText(null);
		    	alert.setContentText(ClientUI.clientLogic.getLastDataRecieved().toString());
		    	alert.showAndWait();
		    	return;
			}
			if(ClientUI.clientLogic.getTypeOfLastDataRecieved() != DataType.DISHES_LIST) {
				System.out.println("Houston, we got a problem!");
				return;
			}
			
			ArrayList<Dish> DishesToShow = (ArrayList<Dish>)ClientUI.clientLogic.getLastDataRecieved();
	    	
			menu = DishesToShow;
			
			if(DishesToShow.size() == 0) return;
			
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
				Button b = new Button("edit dish");
				ControllerFX_EditMenuscreen editMenu = this;
				b.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		            	IClientFxController nextScreen = new ControllerFX_EditDishScreen();
		            	ControllerFX_EditDishScreen.dish = d;
		            	ControllerFX_EditDishScreen.menu = menu;    	
		            	ClientUI.historyStack.pushFxController(editMenu);
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
			ScrollPane firstsScroll = new ScrollPane();
			firstsScroll.setContent(firtsVbox);
			firstsTab.setContent(firstsScroll);
			
			ScrollPane mainScroll = new ScrollPane();
			mainScroll.setContent(mainVbox);
			mainDishTab.setContent(mainScroll);
			
			ScrollPane dessertsScroll = new ScrollPane();
			dessertsScroll.setContent(dessertsVbox);
			dessertsTab.setContent(dessertsScroll);
			
			ScrollPane drinksScroll = new ScrollPane();
			drinksScroll.setContent(drinksVbox);
			drinksTab.setContent(drinksScroll);
		
	}

}


