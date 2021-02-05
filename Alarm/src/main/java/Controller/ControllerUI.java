package Controller;

import Model.Alarm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

/**
 * Controlador da aplicação.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
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

    /**
     * Aplica as definições introduzidas.
     */
    @FXML
    void applySettings() {
        try {
            Alarm alarm = Alarm.getInstance();
            alarm.setCommand(command.getText());
            alarm.setDoCommand(doCommand.isSelected());
            alarm.setEmail(email.getText());
            alarm.setDoEmail(doEmail.isSelected());
            alarm.setCpuThreshold(Double.parseDouble(cpu.getText()));
            alarm.setMemThreshold(Double.parseDouble(mem.getText()));
            Alarm.getInstance().printText("Settings changed.");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Começa a aplicação.
     *
     * @param event Usado para abrir o selecionador de arquivos.
     */
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
                Alarm alarm = new Alarm(selectedFile, textArea);
                new Thread(() -> {
                    try {
                        alarm.read();
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
