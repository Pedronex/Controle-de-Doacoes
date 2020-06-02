package ads.pucgo.view.controllers;

import ads.pucgo.model.Doacao;
import ads.pucgo.model.DoacaoBean;
import ads.pucgo.service.ServiceDoacao;
import ads.pucgo.service.ServiceDoacaoImpl;
import ads.pucgo.util.DoacaoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    Logger logger;
    ServiceDoacao service = new ServiceDoacaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarTabela();

        tabelaDoacao.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Doacao doacao = service.consultar(newValue.getId());
                logger.log(Level.INFO,doacao.getDataDoacao().toString());
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
        });
    }

    private void carregarTabela(){
        ObservableList<DoacaoBean> lista = FXCollections.observableArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(2);
        try {
            for (Doacao doacao:service.listar()) {
                DoacaoBean bean = new DoacaoBean();
                bean.setId(doacao.getId());
                bean.setCpfBeneficiario(doacao.getCpf());
                bean.setNomeBeneficiario(doacao.getNomeBeneficiario());
                bean.setValorDoacao(numberFormat.format(doacao.getValorDoado()));
                bean.setDataDoacao(sdf.format(doacao.getDataDoacao()));
                bean.setInstituicaoDoadora(doacao.getInstituicaoDoadora());
                bean.setDataEntrada(sdf.format(doacao.getDataEntrada()));
                lista.add(bean);
            }
            lista.addAll();
            if (!lista.isEmpty()) {
                tabelaDoacao.setItems(lista);
                clCpf.setCellValueFactory(new PropertyValueFactory<>("cpfBeneficiario"));
                clBeneficiario.setCellValueFactory(new PropertyValueFactory<>("nomeBeneficiario"));
                clDataDoacao.setCellValueFactory(new PropertyValueFactory<>("dataDoacao"));
                clInstituicao.setCellValueFactory(new PropertyValueFactory<>("instituicaoDoadora"));
                clValorDoado.setCellValueFactory(new PropertyValueFactory<>("valorDoacao"));
            }
        } catch (DoacaoException e) {
            e.printStackTrace();
        }
    }
}
