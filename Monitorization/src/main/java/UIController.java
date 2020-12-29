import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UIController {
    @FXML
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private TextField log;
    @FXML
    private Button start;
    @FXML
    private TextArea text;

    @FXML
    void startMonitoring(ActionEvent event) {
        Monitorization monitor = new Monitorization(address.getText(), port.getText(), log.getText(),text);
        try {
            monitor.start();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
