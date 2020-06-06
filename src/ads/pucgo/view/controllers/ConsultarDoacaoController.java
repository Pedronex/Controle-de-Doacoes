package ads.pucgo.view.controllers;

import ads.pucgo.model.Doacao;
import ads.pucgo.model.DoacaoBean;
import ads.pucgo.service.ServiceDoacao;
import ads.pucgo.service.ServiceDoacaoImpl;
import ads.pucgo.util.DoacaoException;
import ads.pucgo.util.MetodosGenericos;
import ads.pucgo.util.TextFieldFormatador;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
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
import java.util.*;

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

    @FXML
    private JFXTextField inpId;

    @FXML
    private JFXTextField inpCpf;

    @FXML
    private JFXTextField inpNome;

    @FXML
    private JFXTextField inpInstituicao;

    @FXML
    private JFXDatePicker inpDataDoacao;


    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnConsultar;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MetodosGenericos.carregarTabela(tabelaDoacao, clCpf, clBeneficiario, clInstituicao, clValorDoado, clDataDoacao);

        configurarCampos();

        btnCancelar.setOnAction(event ->
                MetodosGenericos.fechar(btnCancelar,getClass().getResource("../layouts/telaInicial.fxml")));

        btnConsultar.setOnAction(event -> {
            try {
                carregarTabelaFiltrada(consultarCampos());
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
            btnConsultar.setFocusTraversable(false);
        });
    }

    private ObservableList<DoacaoBean> consultarCampos() throws DoacaoException {
        ServiceDoacao service = new ServiceDoacaoImpl();
        ObservableList<DoacaoBean> beans = FXCollections.observableArrayList();
        List<Doacao> doacoes = service.listar();

        for (Doacao doacao : doacoes) {
            if (inpId.getText().equals(doacao.getId().toString())){
                beans.add(setBean(doacao));
            }
            if (inpCpf.getText().equalsIgnoreCase(doacao.getCpf())){
                beans.add(setBean(doacao));
            }
            if (inpDataDoacao.getEditor().getText().equals(sdf.format(doacao.getDataDoacao()))){
                beans.add(setBean(doacao));
            }
            if (inpNome.getText().equalsIgnoreCase(doacao.getNomeBeneficiario())){
                beans.add(setBean(doacao));
            }
            if (inpInstituicao.getText().equalsIgnoreCase(doacao.getInstituicaoDoadora())) {
                beans.add(setBean(doacao));
            }
        }
        return removerDuplicatas(beans);
    }

    private ObservableList<DoacaoBean> removerDuplicatas(ObservableList<DoacaoBean> beans){
        List<DoacaoBean> novaLista = new ArrayList<>();
        for (DoacaoBean bean : beans) {
            if (novaLista.isEmpty()) {
                novaLista.add(bean);
            } else {
                int count = 0;
                for (DoacaoBean f : novaLista) {
                    if (bean.getId().equals(f.getId())) {
                        count++;
                    }
                }
                if (count == 0) {
                    novaLista.add(bean);
                }
            }
        }
        return FXCollections.observableArrayList(novaLista);
    }

    private DoacaoBean setBean(Doacao doacao) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        DoacaoBean bean = new DoacaoBean();
        bean.setId(doacao.getId());
        bean.setCpfBeneficiario(doacao.getCpf());
        bean.setDataEntrada(sdf.format(doacao.getDataEntrada()));
        bean.setInstituicaoDoadora(doacao.getInstituicaoDoadora());
        bean.setDataDoacao(sdf.format(doacao.getDataDoacao()));
        bean.setValorDoacao(numberFormat.format(doacao.getValorDoado()));
        bean.setNomeBeneficiario(doacao.getNomeBeneficiario());
        return bean;
    }

    private void carregarTabelaFiltrada(ObservableList<DoacaoBean> lista) {
        if (lista.isEmpty()) {
            MetodosGenericos.carregarTabela(tabelaDoacao, clCpf, clBeneficiario, clInstituicao, clValorDoado, clDataDoacao);
        } else {
            tabelaDoacao.setItems(lista);
            clCpf.setCellValueFactory(new PropertyValueFactory<>("cpfBeneficiario"));
            clBeneficiario.setCellValueFactory(new PropertyValueFactory<>("nomeBeneficiario"));
            clDataDoacao.setCellValueFactory(new PropertyValueFactory<>("dataDoacao"));
            clInstituicao.setCellValueFactory(new PropertyValueFactory<>("instituicaoDoadora"));
            clValorDoado.setCellValueFactory(new PropertyValueFactory<>("valorDoacao"));
        }
    }

    private void configurarCampos() {
        TextFieldFormatador formatador = new TextFieldFormatador();
        formatador.setMascara("###.###.###-##");
        formatador.setCaracteresValidos("0123456789");
        // Ultilizando mascara
        inpCpf.setOnKeyReleased(event -> {
            formatador.setTexto(inpCpf);
            try {
                formatador.formatar();
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
        });

        inpNome.textProperty().addListener((observable, oldValue, newValue) ->
                validarNome(inpNome, oldValue, newValue));

        inpInstituicao.textProperty().addListener((observable, oldValue, newValue) ->
                validarNome(inpInstituicao, oldValue, newValue));

        inpCpf.textProperty().addListener((observable, oldValue, newValue) ->
                validarCPF(inpCpf, oldValue, newValue));
    }

    private void validarNome(JFXTextField field, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            if (!newValue.matches("[A-Z a-zÀ-Ÿà-ÿ]+") || oldValue.length() != 0) {
                field.setText("");
            }
        } else if (!newValue.matches("[A-Z a-zÀ-Ÿà-ÿ]+") || newValue.length() > 50) {
            field.setText(oldValue);
        }
    }

    private void validarCPF(JFXTextField field, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            if (!newValue.matches("[0-9.\\-]+") || oldValue.length() != 0)
                field.setText("");
        } else if (!newValue.matches("[0-9.\\-]+") || newValue.length() > 50)
            field.setText(oldValue);
    }
}
