package Controllers;

import Model.Agregador;
import Model.Process;
import eu.hansolo.medusa.Gauge;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.io.IOException;
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
    private Gauge cpuG;
    @FXML
    private Gauge ramG;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cpuG.setValue(Agregador.getInstance().getLastCPUTotal());
        cpuG.setAnimationDuration(1000);
        ramG.setValue(Agregador.getInstance().getLastMEMTotal());
        cpuG.setAnimationDuration(1000);
        startTable();
    }

    public void startTable() {
        Agregador ag = Agregador.getInstance();
        ObservableList<Process> lista = ag.getLastProcesses();

        this.PID.setCellValueFactory(new PropertyValueFactory<>("pid"));
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.cpu.setCellValueFactory(new PropertyValueFactory<>("cpu"));
        this.ram.setCellValueFactory(new PropertyValueFactory<>("mem"));

        this.processTable.setItems(lista);
        lista.addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                cpuG.setValue(Agregador.getInstance().getLastCPUTotal());
                ramG.setValue(Agregador.getInstance().getLastMEMTotal());
            }
        });
    }

    @FXML
    public void memClick(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("mem.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("material.css");
            Stage stage = new Stage();
            stage.setTitle("RAM");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void cpuClick(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("cpu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("material.css");
            Stage stage = new Stage();
            stage.setTitle("CPU");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void exitApp(MouseEvent event) {

    }
}
