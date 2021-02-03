package Controller;

import Model.Agregador;
import Model.Process;
import eu.hansolo.medusa.Gauge;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

/**
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class ControllerUI implements Initializable {

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
    @FXML
    private Label infoText;


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
            fxmlLoader.setLocation(getClass().getResource("/View/mem.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("/StyleSheet/material.css");
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
            fxmlLoader.setLocation(getClass().getResource("/View/cpu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("/StyleSheet/material.css");
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
    void infoClick(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View/info.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("/StyleSheet/material.css");
            Stage stage = new Stage();
            stage.setTitle("Info");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void processSelected(MouseEvent event) {
        Process process = processTable.getSelectionModel().getSelectedItem();
        String text = "Name: ";
        text += process.getName() + '\n';
        text += "First uptime: ";
        text += Agregador.getInstance().getFirstProcessUptime(process.getPid()) + '\n';
        text += "Time up: ";
        try {
            text += Agregador.getInstance().getTotalTimeProcess(process.getPid())+"s";
        } catch (ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        infoText.setText(text);
    }


    @FXML
    void exitApp(MouseEvent event) {
        Platform.exit();
        System.exit(0);
    }
}
