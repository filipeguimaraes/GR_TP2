package Controller;

import Model.Agregador;
import Model.Estado;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador da view com o gráfico referente à memória da aplicação.
 *
 * @author Filipe Miguel Teixeira Freitas Guimarães - A865308
 */
public class ControllerMem implements Initializable {

    @FXML
    private AreaChart<String, Number> chart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    /**
     *  Criar a tabela com os dados referentes á memória.
     *
     * @param url Ignored.
     * @param resourceBundle Ignored.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xAxis.setLabel("Time");
        yAxis.setLabel("%");
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickUnit(10);
        XYChart.Series<String,Number> mem = new XYChart.Series<>();
        for (Estado e : Agregador.getInstance().getEstados()){
            mem.getData().add(new XYChart.Data<>(e.getUptime(),e.getRamTotal()));
        }

        chart.getData().add(mem);

    }
}
