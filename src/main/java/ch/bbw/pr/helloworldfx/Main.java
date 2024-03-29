package ch.bbw.pr.helloworldfx;
	
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


/**
 * @author S. Sturzenegger & T. Tanner
 * @version 16.01.2021
 */


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Model model = new Model();
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("View.fxml"));
			AnchorPane root = myLoader.load();
			Controller controller = myLoader.getController();
			controller.setModel(model);

			Scene scene = new Scene(root,600,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Game of Life");
			primaryStage.setScene(scene);
			primaryStage.show();
			Thread.sleep(4000);
			controller.loop();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		System.out.println("JavaFX " + System.getProperty("javafx.version") + ", running on Java " + System.getProperty("java.version") + ".");
		launch(args);

	}
}
