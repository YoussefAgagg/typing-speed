package com.github.youssefagagg.typing;

import java.util.Locale;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainTypeKeyBoard extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		Locale.setDefault(Locale.US);
		
		Parent root = FXMLLoader.load(getClass().getResource("typefx.fxml" ));
		Scene scene=new Scene(root);
		
		stage.setTitle("type app");
		stage.setScene(scene);
		stage.show();
		
	}
	public static void main(String args[])
	{
		launch(args);
	}

}

