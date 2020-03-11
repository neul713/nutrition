package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AppMain extends Application {
   
   public static Stage mainStage = null;
   
   public static void main(String[] args) {
      launch(args);
   }
   @Override
   public void start(Stage primaryStage){
      Parent root;
      Scene scene;
      try {
         root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
         scene = new Scene(root);
         mainStage = primaryStage;
         primaryStage.setScene(scene);
         primaryStage.setTitle("·Î±×ÀÎ");
         primaryStage.setResizable(false);
         primaryStage.show();
      } catch (IOException e) {
         e.printStackTrace();
      }
      
   }
}