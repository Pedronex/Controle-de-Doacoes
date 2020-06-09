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

/**
 * Classe Controladora da tela principal da aplicação
 * @author Pedro Soares
 */
public class PrincipalController implements Initializable {

    @FXML
    private JFXButton btnCriar;

    @FXML
    private JFXButton btnAtualizar;

    @FXML
    private JFXButton btnConsultar;

    @FXML
    private Pane principalPane;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        // Chamada do método que retira o foco nos botões
        removerFoco();

        // Chamada do método que chama a tela de criação
        btnCriar.setOnAction(event -> {
            try {
                chamarTela("novaDoacao");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Chamada do método que chama a tela de consulta
        btnConsultar.setOnAction(event -> {
            try {
                chamarTela("consultarDoacao");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Chamada do método que chama a tela de consulta
        btnAtualizar.setOnAction(event -> {
            try {
                chamarTela("alterarDoacao");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Método para chamar a tela
     * @param nomeTela uma String com o nome da tela que será chamada
     * @throws IOException exceção que está na assinatura do método load do FXMLLoader
     */
    private void chamarTela(String nomeTela) throws IOException {
        Parent tela = FXMLLoader.load(getClass().getResource("../layouts/" + nomeTela + ".fxml"));
        principalPane.getChildren().setAll(tela);
    }

    /**
     * Método para remover o foco dos botões
     */
    private void removerFoco(){
        btnCriar.setFocusTraversable(false);
        btnAtualizar.setFocusTraversable(false);
        btnConsultar.setFocusTraversable(false);
    }
}
