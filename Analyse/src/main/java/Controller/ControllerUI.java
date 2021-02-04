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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

/**
 * Controlador da view principal da aplicação.
 *
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

    /**
     * Inicializar as tabelas e velocímetros.
     *
     * @param url            ignored.
     * @param resourceBundle ignored.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cpuG.setValue(Agregador.getInstance().getLastCPUTotal());
        cpuG.setAnimationDuration(1000);
        ramG.setValue(Agregador.getInstance().getLastMEMTotal());
        cpuG.setAnimationDuration(1000);
        startTable();
    }

    /**
     * Preencher a tabela com os respetivos dados.
     */
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

    /**
     * Abrir o gráfico da memória.
     */
    @FXML
    public void memClick() {
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

    /**
     * Abrir o gráfico do CPU.
     */
    @FXML
    public void cpuClick() {
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

    /**
     * Abrir a view das infos.
     */
    @FXML
    void infoClick() {
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

    /**
     * Chamado quando é selecionado um processo.
     */
    @FXML
    void processSelected() {
        Process process = processTable.getSelectionModel().getSelectedItem();
        String text = "Name: ";
        text += process.getName() + '\n';
        text += "First uptime: ";
        text += Agregador.getInstance().getFirstProcessUptime(process.getPid()) + '\n';
        text += "Time up: ";
        try {
            text += Agregador.getInstance().getTotalTimeProcess(process.getPid()) + "s";
        } catch (ParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        infoText.setText(text);
    }

    /**
     * Sair da app terminado todas as threads ativas.
     */
    @FXML
    void exitApp() {
        Platform.exit();
        System.exit(0);
    }
}
