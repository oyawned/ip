package sheng;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Sheng using FXML.
 */
public class Main extends Application {

    private Sheng sheng = new Sheng();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            
            // Add custom CSS for styling
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
            
            stage.setScene(scene);
            stage.setTitle("Sheng Chatbot");
            stage.setMinWidth(350);
            stage.setMinHeight(400);
            stage.setResizable(true);
            
            fxmlLoader.<MainWindow>getController().setSheng(sheng);  // inject the Sheng instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
