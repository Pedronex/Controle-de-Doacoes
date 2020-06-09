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

/**
 * Classe controladora da tela de consulta
 *
 * @author Pedro Soares
 */
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

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final ServiceDoacao service = new ServiceDoacaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Chamada do método que carrega a tabela na interface grafica
        carregarTabela();
        // Chamada do método de configuração dos campos da tela
        configurarCampos();

        // Ação de fechar a tela atual
        btnCancelar.setOnAction(event ->
                MetodosGenericos.fechar(btnCancelar, getClass().getResource("../layouts/telaInicial.fxml")));

        // Ação de filtrar os itens da tabela
        btnConsultar.setOnAction(event -> {
            try {
                carregarTabelaFiltrada(consultarCampos());
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
            // Função de retirar o foco do botão
            btnConsultar.setFocusTraversable(false);
        });
    }

    private void carregarTabela() {
        ObservableList<DoacaoBean> lista = FXCollections.observableArrayList();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(2);
        try {
            for (Doacao item : service.listar()) {
                DoacaoBean bean = new DoacaoBean();
                bean.setId(item.getId());
                bean.setCpfBeneficiario(item.getCpf());
                bean.setNomeBeneficiario(item.getNomeBeneficiario());
                bean.setValorDoacao(numberFormat.format(item.getValorDoado()));
                bean.setDataDoacao(sdf.format(item.getDataDoacao()));
                bean.setInstituicaoDoadora(item.getInstituicaoDoadora());
                bean.setDataEntrada(sdf.format(item.getDataEntrada()));
                lista.add(bean);
            }
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

    /**
     * Método de filtragem da lista de doações
     *
     * @return lista filtrada sem itens duplicados
     * @throws DoacaoException classe de exceção personalizado para apresentar o erro para o usuario
     */
    private ObservableList<DoacaoBean> consultarCampos() throws DoacaoException {
        ObservableList<DoacaoBean> beans = FXCollections.observableArrayList();
        List<Doacao> doacoes = service.listar();

        for (Doacao doacao : doacoes) {
            if (inpId.getText().equals(doacao.getId().toString())) {
                beans.add(setBean(doacao));
            }
            if (inpCpf.getText().equalsIgnoreCase(doacao.getCpf())) {
                beans.add(setBean(doacao));
            }
            if (inpDataDoacao.getEditor().getText().equals(sdf.format(doacao.getDataDoacao()))) {
                beans.add(setBean(doacao));
            }
            if (inpNome.getText().equalsIgnoreCase(doacao.getNomeBeneficiario())) {
                beans.add(setBean(doacao));
            }
            if (inpInstituicao.getText().equalsIgnoreCase(doacao.getInstituicaoDoadora())) {
                beans.add(setBean(doacao));
            }
        }
        return removerDuplicatas(beans);
    }

    /**
     * Método para remover itens duplicados
     *
     * @param beans lista de Doações
     * @return Lista de doações sem
     */
    private ObservableList<DoacaoBean> removerDuplicatas(ObservableList<DoacaoBean> beans) {
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

    /**
     * Este método grava os dados coletados da doação em um bean
     *
     * @param doacao doação coletada da lista
     * @return Bean da doação realizada
     */
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

    /**
     * Método que carrega os itens filtrados na tela
     *
     * @param lista Lista de itens que serão apresentados na tela
     */
    private void carregarTabelaFiltrada(ObservableList<DoacaoBean> lista) {
        if (lista.isEmpty()) {
            carregarTabela();
        } else {
            tabelaDoacao.setItems(lista);
            clCpf.setCellValueFactory(new PropertyValueFactory<>("cpfBeneficiario"));
            clBeneficiario.setCellValueFactory(new PropertyValueFactory<>("nomeBeneficiario"));
            clDataDoacao.setCellValueFactory(new PropertyValueFactory<>("dataDoacao"));
            clInstituicao.setCellValueFactory(new PropertyValueFactory<>("instituicaoDoadora"));
            clValorDoado.setCellValueFactory(new PropertyValueFactory<>("valorDoacao"));
        }
    }

    /**
     * Configura os campos do formulário
     */
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
