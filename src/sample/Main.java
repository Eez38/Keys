package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private Pane mainLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JH Keys");

        initRootLayout();

//        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
//        primaryStage.setScene(new Scene(root, 900, 450));
//        primaryStage.show();
    }


    private void initRootLayout() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("main_views/main.fxml"));
//            loader.setController(Controller.class);
            mainLayout = (Pane) loader.load();
            primaryStage.setScene(new Scene(mainLayout));
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
