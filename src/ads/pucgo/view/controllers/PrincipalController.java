package ads.pucgo.view.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {

    @FXML
    private JFXButton btnCriar;

    @FXML
    private JFXButton btnAtualizar;

    @FXML
    private JFXButton btnExcluir;

    @FXML
    private JFXButton btnConsultar;

    @FXML
    private Pane principalPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCriar.setFocusTraversable(false);
        btnAtualizar.setFocusTraversable(false);
        btnConsultar.setFocusTraversable(false);
        btnExcluir.setFocusTraversable(false);

        btnCriar.setOnAction(event -> {
            try {
                chamarTela("novaDoacao");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnConsultar.setOnAction(event -> {
            try {
                chamarTela("consultarDoacao");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnAtualizar.setOnAction(event -> {
            try {
                chamarTela("alterarDoacao");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnExcluir.setOnAction(event -> {
            try {
                chamarTela("deletarDoacao");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void chamarTela(String nomeTela) throws IOException {
        Parent tela = FXMLLoader.load(getClass().getResource("../layouts/" + nomeTela + ".fxml"));
        principalPane.getChildren().setAll(tela);
    }
}
