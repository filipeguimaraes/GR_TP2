import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private TextArea text;

    @FXML
    void startMonitoring(ActionEvent event) {
        onoff.setImage(new Image("images/on.png"));
        Monitorization monitor = new Monitorization(address.getText(), port.getText(), text);
        try {
            monitor.start();
        } catch (Exception e) {
            onoff.setImage(new Image("images/off.png"));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


}
