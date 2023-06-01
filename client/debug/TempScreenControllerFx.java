package client.debug;

import java.io.IOException;

import client.interfaces.IClientFxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TempScreenControllerFx implements IClientFxController{

	@Override
	public void start(Stage stage) {
		Stage primaryStage  = stage;
		if(primaryStage == null) {
			primaryStage = new Stage();
		}
        Parent root = null;
        try {
			root = FXMLLoader.load(getClass().getResource("/client/debug/TempScreenFxml.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
        Scene scene = new Scene(root);
//      scene.getStylesheets().add(getClass().getResource("/client/client_gui/LoginFxml.css").toExternalForm());
        primaryStage.setTitle("TempScreen");
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}
