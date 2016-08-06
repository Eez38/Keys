import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Eez on 28/07/2016.
 */
public class JHSplash extends Preloader {

    ProgressBar bar;
    Stage stage;

    public static void main(String[] args) throws Exception { launch(args); }

    private Scene createScene(){
        Image logo = new Image("file:images/jesushouse_logo-250x150.jpg");
        ImageView imageView = new ImageView(logo);
//        imageView.setFitWidth(400);
        imageView.fitWidthProperty().bind(stage.widthProperty().divide(2));
        bar = new ProgressBar();
        bar.setPrefWidth(400);
        VBox vBox = new VBox(imageView, bar);
        vBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);
        return new Scene(borderPane, 600, 300);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        stage.setScene(createScene());
        stage.show();
    }

    @Override
    public void handleProgressNotification (ProgressNotification progressNotification){
        bar.setProgress(progressNotification.getProgress());
    }

    @Override
    public void handleStateChangeNotification (StateChangeNotification stateChangeNotification){
        if(stateChangeNotification.getType() == StateChangeNotification.Type.BEFORE_START){
            stage.hide();
        }
    }
}
