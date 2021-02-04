package Controller;

import Model.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class ControllerMain {

    @FXML
    private Button file;
    @FXML
    private Button start;
    @FXML
    private ProgressIndicator progress;

    /**
     *
     * @param event
     */
    @FXML
    void chooseFile(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);
        if (selectedFile != null &&
                selectedFile.getPath().contains("log") &&
                selectedFile.getPath().contains(".txt")) {
            Log log = new Log(selectedFile);
            try {
                log.open();
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            this.progress.setVisible(true);
            try {
                log.init();
            } catch (IOException | InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Não foi possível ler o ficheiro.");
                alert.showAndWait();
            }
            this.progress.setVisible(false);
            new Thread(() -> {
                try {
                    log.read();
                } catch (IOException | InterruptedException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Não foi possível ler o ficheiro.");
                    alert.showAndWait();
                }
            }).start();
            file.setDisable(true);
            start.setDisable(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ficheiro inválido.");
            alert.showAndWait();
        }
    }

    /**
     *
     * @param event
     */
    @FXML
    void start(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setTitle("\"mytop\"");
        Image image = new Image("/images/icon.png");
        stage.getIcons().add(image);
        try {
            Scene start = new Scene(FXMLLoader.load(getClass().getResource("/View/ui.fxml")));
            start.getStylesheets().add("/StyleSheet/material.css");
            stage.setScene(start);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}

