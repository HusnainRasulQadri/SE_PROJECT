package frontendLayer;

import buisnessLayer.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class message {
	@FXML
	private ImageView closeButton;
	@FXML
	private ImageView minimizeButton;
	Stage stage;

	void getStage(Stage stage) throws InterruptedException, IOException {
		this.stage = stage;
		closeButton.setOnMouseClicked(mouseEvent -> stage.close());
		minimizeButton.setOnMouseClicked(mouseEvent -> stage.setIconified(true));
	}

	@FXML
	void clicked () throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 509, 552);
		stage.setScene(scene);
		((menu) fxmlLoader.getController()).getStage(stage);
		stage.show();
	}

}
