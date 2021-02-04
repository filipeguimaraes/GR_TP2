package Controller;

import Model.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ControllerUI {

    @FXML
    private Button start;
    @FXML
    private TextArea textArea;
    @FXML
    private CheckBox doEmail;
    @FXML
    private CheckBox doCommand;
    @FXML
    private TextField cpu;
    @FXML
    private TextField mem;
    @FXML
    private TextField email;
    @FXML
    private TextField command;
    @FXML
    private Button apply;

    @FXML
    void applySettings(ActionEvent event) {
        try {
            Log log = Log.getInstance();
            log.setCommand(command.getText());
            log.setDoCommand(doCommand.isSelected());
            log.setEmail(email.getText());
            log.setDoEmail(doEmail.isSelected());
            log.setCpuThreshold(Double.parseDouble(cpu.getText()));
            log.setMemThreshold(Double.parseDouble(mem.getText()));
            textArea.setText(textArea.getText()+"Settings changed.\n");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void start(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);
        if (selectedFile != null &&
                selectedFile.getPath().contains("log") &&
                selectedFile.getPath().contains(".txt")) {
            try {
                Log log = new Log(selectedFile,textArea);
                new Thread(() -> {
                    try {
                        log.read();
                    } catch (IOException | InterruptedException | MessagingException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Não foi possível ler o ficheiro.");
                        alert.showAndWait();
                    }
                }).start();
                apply.setDisable(false);
                start.setDisable(true);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ficheiro inválido.");
            alert.showAndWait();
        }

    }


}
