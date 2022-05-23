package frontendLayer;

import buisnessLayer.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class footballAvailability implements Initializable{
	@FXML
	private ImageView closeButton;
	@FXML
	private ImageView minimizeButton;
	Stage stage;

	@FXML
	Label message;

	void getStage(Stage stage) throws InterruptedException, IOException {
		this.stage = stage;
		closeButton.setOnMouseClicked(mouseEvent -> stage.close());
		minimizeButton.setOnMouseClicked(mouseEvent -> stage.setIconified(true));
	}

	@FXML
	void back() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menuAdmin.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 509, 552);
		stage.setScene(scene);
		((menuAdmin) fxmlLoader.getController()).getStage(stage);
		stage.show();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		if (Objects.equals(Controller.checkFootballAvailablity(), "true")) {
			message.setText("Football Ground Made Unavailable");
		}
		else {
			message.setText("Football Ground Made Available");
		}
		Controller.changeFootballAvailability();
	}
}
