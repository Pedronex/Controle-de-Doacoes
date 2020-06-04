package ads.pucgo.view.controllers;

import ads.pucgo.model.DoacaoBean;
import ads.pucgo.util.MetodosGenericos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsultarDoacaoController implements Initializable {

    @FXML
    private TableView<DoacaoBean> tabelaDoacao;

    @FXML
    private TableColumn<DoacaoBean, String> clCpf;

    @FXML
    private TableColumn<DoacaoBean, String> clBeneficiario;

    @FXML
    private TableColumn<DoacaoBean, String> clInstituicao;

    @FXML
    private TableColumn<DoacaoBean, String> clValorDoado;

    @FXML
    private TableColumn<DoacaoBean, String> clDataDoacao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MetodosGenericos.carregarTabela(tabelaDoacao,clCpf,clBeneficiario,clInstituicao,clValorDoado,clDataDoacao);
        tabelaDoacao.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

        });
    }
}
