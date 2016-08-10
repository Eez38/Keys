package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Ese Emore (08/2016)
 */
public class Main extends Application {

    private Stage primaryStage;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JH Keys");

        initRootLayout();

    }


    private void initRootLayout() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("main_views/main.fxml"));
            Pane mainLayout = loader.load();
            scene = new Scene(mainLayout);

//            scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
//                System.out.println("Width: " + newSceneWidth);
//            });
//            scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
//                System.out.println("Height: " + newSceneHeight);
//            });

            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
