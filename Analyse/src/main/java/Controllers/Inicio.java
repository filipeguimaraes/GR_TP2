package Controllers;

import Model.Agregador;
import Model.Process;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Inicio implements Initializable {

    @FXML
    private TableView<Process> processTable;
    @FXML
    private TableColumn<Process, Integer> PID;
    @FXML
    private TableColumn<Process, String> name;
    @FXML
    private TableColumn<Process, Double> cpu;
    @FXML
    private TableColumn<Process, Double> ram;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTable();
    }

    public void startTable() {
        Agregador ag = Agregador.getInstance();
        ObservableList<Process> lista = FXCollections.observableArrayList(ag.getLastEstado().getProcessos());

        this.PID.setCellValueFactory(new PropertyValueFactory<>("pid"));
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.cpu.setCellValueFactory(new PropertyValueFactory<>("cpu"));
        this.ram.setCellValueFactory(new PropertyValueFactory<>("mem"));

        this.processTable.setItems(lista);
    }
}
