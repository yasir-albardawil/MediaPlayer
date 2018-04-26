import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
       try {
            Parent root = FXMLLoader.load(getClass().getResource("views/mediaPlayer.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Media player");
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icons/video-player.png")));
           // primaryStage.initStyle(StageStyle.UNDECORATED);
           // primaryStage.setAlwaysOnTop(true);
           primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
