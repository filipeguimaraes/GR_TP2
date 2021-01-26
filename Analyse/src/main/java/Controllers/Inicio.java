package Controllers;

import Model.Agregador;
import Model.Process;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.maven.model.Model;

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
    @FXML
    private Button cpuButton;
    @FXML
    private Button ramButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTable();
    }

    public void startTable() {
        Agregador ag = Agregador.getInstance();
        ObservableList<Process> lista = FXCollections.observableArrayList(ag.getLastEstado().getProcessos());


        //this.PID = new TableColumn<>("PID");
        this.PID.setCellValueFactory(new PropertyValueFactory<>("pid"));
        //this.name = new TableColumn<>("Name");
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //this.cpu = new TableColumn<>("CPU");
        this.cpu.setCellValueFactory(new PropertyValueFactory<>("cpu"));
        //this.ram = new TableColumn<>("RAM");
        this.ram.setCellValueFactory(new PropertyValueFactory<>("mem"));


        this.processTable.setItems(lista);
    }
}
