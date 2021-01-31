package Controller;

import Model.Monitor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
        this.onoff.setImage(new Image("images/on.png"));
        Monitor monitor = Monitor.getInstance();
        try {
            monitor.start(this.address.getText(),
                    this.port.getText(),
                    this.community.getText(),
                    this.text);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void stopMonitoring(ActionEvent event) {
        Monitor.getInstance().join(this.address.getText(), this.port.getText(), this.text);
        if (!Monitor.getInstance().haveThreads()) {
            this.onoff.setImage(new Image("images/off.png"));
        }
    }


    @FXML
    void changeSettings(MouseEvent event) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Settings");
            Image image = new Image("/images/settings.png");
            stage.getIcons().add(image);
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/View/update.fxml")));
            scene.getStylesheets().add("StyleSheet/material.css");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
