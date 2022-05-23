package frontendLayer;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class addSportEquipment {
	@FXML
	TextField name;

	@FXML
	TextField quantity;

	@FXML
	private ImageView closeButton;
	@FXML
	private ImageView minimizeButton;
	private Stage stage;
	void getStage(Stage stage){
		this.stage = stage;
		closeButton.setOnMouseClicked(mouseEvent -> stage.close());
		minimizeButton.setOnMouseClicked(mouseEvent -> stage.setIconified(true));
	}

	@FXML
	void add() {
		String Name = name.toString();
		String Quatity = quantity.toString();
	}
}
