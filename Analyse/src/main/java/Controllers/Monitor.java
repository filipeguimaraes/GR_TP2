package Controllers;

import Model.Agregador;
import Model.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Monitor {

    @FXML
    private Button file;
    @FXML
    private Button start;

    @FXML
    void chooseFile(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);
        Log log = new Log(selectedFile);
        try {
            log.open();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        new Thread(() -> {
            try {
                log.read(Agregador.getInstance());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        start.setDisable(false);
    }

    @FXML
    void start(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("\"mytop\"");
        Image image = new Image("/images/icon.png");
        stage.getIcons().add(image);
        Scene start = null;
        try {
            start = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("inicio.fxml")));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        start.getStylesheets().add("material.css");
        stage.setScene(start);
        stage.centerOnScreen();
        stage.show();
    }

}

