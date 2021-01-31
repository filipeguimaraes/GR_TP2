package Controller;

import Model.Monitor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class updateController {

    @FXML
    private TextField time;

    @FXML
    private Button apply;

    @FXML
    void applyTime(ActionEvent event) {
        Monitor monitor = Monitor.getInstance();
        monitor.changePulling(Integer.parseInt(time.getText()));
        Node source = (Node) event.getSource();
        Window theStage = source.getScene().getWindow();
        Stage stage = (Stage) theStage.getScene().getWindow();
        stage.close();
    }

}