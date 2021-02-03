package Controller;

import Model.Agregador;
import Model.ProcessGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.text.ParseException;

public class ControllerInfo {

    @FXML
    private TextField searchName;

    @FXML
    private ProgressIndicator loading;

    @FXML
    private TableView<ProcessGroup> processos;

    @FXML
    private TableColumn<ProcessGroup, Number> pid;

    @FXML
    private TableColumn<ProcessGroup, String> name;

    @FXML
    private TableColumn<ProcessGroup, Number> cpu;

    @FXML
    private TableColumn<ProcessGroup, Number> mem;

    @FXML
    private TableColumn<ProcessGroup, Number> uptime;


    @FXML
    void doIT(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            search(null);
        }
    }

    @FXML
    void search(MouseEvent event) {
        this.loading.setVisible(true);
        try {
            ObservableList<ProcessGroup> pg =
                    FXCollections.observableArrayList(Agregador
                            .getInstance()
                            .getProcessGroupByName(searchName.getText()));

            this.pid.setCellValueFactory(new PropertyValueFactory<>("pid"));
            this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
            this.cpu.setCellValueFactory(new PropertyValueFactory<>("cpu"));
            this.mem.setCellValueFactory(new PropertyValueFactory<>("mem"));
            this.uptime.setCellValueFactory(new PropertyValueFactory<>("uptime"));

            this.processos.setItems(pg);
        } catch (ParseException e) {
            this.loading.setVisible(false);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        this.loading.setVisible(false);

    }

}
