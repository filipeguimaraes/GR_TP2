import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UIController {
    @FXML
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private ImageView onoff;
    @FXML
    private Button start;
    @FXML
    private Button stop;
    @FXML
    private TextField community;
    @FXML
    private TextArea text;
    @FXML
    private ImageView settings;

    @FXML
    void startMonitoring(ActionEvent event) {
        onoff.setImage(new Image("images/on.png"));
        Monitorization monitor = Monitorization.getInstance();
        try {
            monitor.start(address.getText(), port.getText(),community.getText(), text);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void stopMonitoring(ActionEvent event) {
        Monitorization monitor = Monitorization.getInstance();
        monitor.join(this.address.getText(),this.port.getText(),this.text);
    }


    @FXML
    void changeSettings(MouseEvent event) {
        try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("update.fxml"));
        Scene scene = null;
        scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("material.css");
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
