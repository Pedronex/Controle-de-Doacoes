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
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Classe controladora da tela de alteração
 * @author Pedro Soares
 */
public class AlterarDoacaoController implements Initializable {
    @FXML
    public TableView<DoacaoBean> tabelaDoacao;
    @FXML
    public TableColumn<DoacaoBean, String> clCpf;
    @FXML
    public TableColumn<DoacaoBean, String> clBeneficiario;
    @FXML
    public TableColumn<DoacaoBean, String> clInstituicao;
    @FXML
    public TableColumn<DoacaoBean, String> clValorDoado;
    @FXML
    public TableColumn<DoacaoBean, String> clDataDoacao;
    @FXML
    public JFXTextField inpId;
    @FXML
    public JFXTextField inpCpf;
    @FXML
    public JFXTextField inpNome;
    @FXML
    public JFXTextField inpInstituicao;
    @FXML
    public JFXButton btnCancelar;
    @FXML
    public JFXButton btnAtualizar;
    @FXML
    public JFXTextField inpValorDoado;
    @FXML
    public JFXDatePicker inpDataBaixa;
    @FXML
    public JFXDatePicker inpDataDoacao;

    @FXML
    private JFXButton btnDeletar;

    ServiceDoacao service = new ServiceDoacaoImpl();
    Doacao doacao;
    // Formatador de data
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Chamada do método de carregamento dos itens da tabela
        carregarTabela();

        // Formatador de numero que está instanciado para a função de formatação em moeda
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(2);

        // Chamada do método de mapear o item selecionado pela tabela
        configuracaoTabela();
        // Chamada do método de configurações dos campos presentes editáveis
        configurarCampos();

        // Chamada da função de atualização dos dados
        btnAtualizar.setOnAction(event -> {
            try {
                atualizarDoacao(setBean());
                service.alterar(doacao);
                carregarTabela();
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
        });

        // Chamada da função voltar para a tela inicial
        btnCancelar.setOnAction(event ->
                MetodosGenericos.fechar(btnCancelar, getClass().getResource("../layouts/telaInicial.fxml")));

        // Chamar função de deleção
        btnDeletar.setOnAction(event -> {
            try {
                atualizarDoacao(setBean());
                service.deletar(doacao.getId());
                carregarTabela();
                limparCampos();
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
        });
    }

    private void carregarTabela(){
        ObservableList<DoacaoBean> lista = FXCollections.observableArrayList();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMinimumFractionDigits(2);
        try {
            for (Doacao item:service.listar()) {
                DoacaoBean bean = new DoacaoBean();
                bean.setId(item.getId());
                bean.setCpfBeneficiario(item.getCpf());
                bean.setNomeBeneficiario(item.getNomeBeneficiario());
                bean.setValorDoacao(numberFormat.format(item.getValorDoado()));
                bean.setDataDoacao(sdf.format(item.getDataDoacao()));
                bean.setInstituicaoDoadora(item.getInstituicaoDoadora());
                lista.add(bean);
            }
            if (!lista.isEmpty()) {
                tabelaDoacao.setItems(lista);
                clCpf.setCellValueFactory(new PropertyValueFactory<>("cpfBeneficiario"));
                clBeneficiario.setCellValueFactory(new PropertyValueFactory<>("nomeBeneficiario"));
                clDataDoacao.setCellValueFactory(new PropertyValueFactory<>("dataDoacao"));
                clInstituicao.setCellValueFactory(new PropertyValueFactory<>("instituicaoDoadora"));
                clValorDoado.setCellValueFactory(new PropertyValueFactory<>("valorDoacao"));
            }else {
                tabelaDoacao.setItems(lista);
            }
        } catch (DoacaoException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Método que realiza o controle da tabela para mapear os dados selecionados
     */
    private void configuracaoTabela() {
        tabelaDoacao.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    doacao = service.consultar(newValue.getId());
                    inpCpf.setText(doacao.getCpf());
                    inpDataBaixa.getEditor().setText(sdf.format(doacao.getDataEntrada()));
                    inpDataDoacao.getEditor().setText(sdf.format(doacao.getDataDoacao()));
                    inpId.setText(doacao.getId().toString());
                    inpNome.setText(doacao.getNomeBeneficiario());
                    inpInstituicao.setText(doacao.getInstituicaoDoadora());
                    inpValorDoado.setText(doacao.getValorDoado().toString());
                } catch (DoacaoException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Método para limpar os campos
     */
    private void limparCampos() {
        inpCpf.setText("");
        inpNome.setText("");
        inpValorDoado.setText("");
        inpInstituicao.setText("");
        inpId.setText("");
        inpDataDoacao.getEditor().setText("");
        inpDataBaixa.getEditor().setText("");
    }

    private void atualizarDoacao(DoacaoBean bean) {
        // Instanciando nova doação
        doacao = new Doacao();
        // Chamando o método de mapeamento do bean para a entidade Doação
        doacao = MetodosGenericos.mapearDoacao(bean);
        // Gravando o ID coletado do bean
        doacao.setId(bean.getId());
    }

    private DoacaoBean setBean() throws DoacaoException {
        // Gravando todos os dados no Bean
        DoacaoBean bean = new DoacaoBean();
        bean.setId(Integer.parseInt(inpId.getText()));
        bean.setCpfBeneficiario(inpCpf.getText());
        bean.setNomeBeneficiario(inpNome.getText());
        bean.setDataDoacao(inpDataDoacao.getEditor().getText());
        bean.setInstituicaoDoadora(inpInstituicao.getText());
        bean.setValorDoacao(inpValorDoado.getText());

        // Validando campos vazios
        if (bean.getCpfBeneficiario().isEmpty())
            throw new DoacaoException("O campo CPF não pode estar vazio!", "Digite apenas numeros");
        if (bean.getNomeBeneficiario().isEmpty())
            throw new DoacaoException("O campo Nome do Beneficiário não pode estar vazio", "O tamanho maximo é 50 caracteres");
        if (bean.getDataDoacao().isEmpty())
            throw new DoacaoException("O campo Data da Doação deve ser preenchida!", "Clique no calendario e selecione uma data");
        if (bean.getInstituicaoDoadora().isEmpty())
            throw new DoacaoException("O campo Instituição doadora não pode estar vazio!", "O tamanho maximo é 50 caracteres");
        if (bean.getValorDoacao().isEmpty())
            bean.setValorDoacao("0");

        // Validações de valores
        if (bean.getDataDoacao().matches("[0-3][0-9]/[0-1][0-9]/[0-9]+")) {
            // validações de limite de data
            Calendar dataMinima = Calendar.getInstance();
            dataMinima.set(1990, Calendar.JANUARY, 1);
            Calendar dataMaxima = Calendar.getInstance();
            dataMaxima.setTimeInMillis(System.currentTimeMillis());
            Calendar dataConvertida = Calendar.getInstance();
            try {
                dataConvertida.setTime(new Date(sdf.parse(bean.getDataDoacao()).getTime()));
            } catch (ParseException e) {
                throw new DoacaoException("Não foi possivel formatar a data",
                        "Tente inserir uma data valida");
            }

            if (dataConvertida.after(dataMaxima)) {
                throw new DoacaoException("Data não pode ser maior que a data atual", "Tente inserir uma doação antiga não no futuro");
            } else if (dataConvertida.before(dataMinima)) {
                throw new DoacaoException("Data não pode ser menos que 01/01/1990", "Tente inserir datas acima da data minima");
            }
            return bean;
        } else {
            throw new DoacaoException("O campo data não entra no formato", "A data não está no formato **/**/****");
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
        // Validações em tempo real
        inpNome.textProperty().addListener((observable, oldValue, newValue) ->
                validarNome(inpNome, oldValue, newValue));

        inpInstituicao.textProperty().addListener((observable, oldValue, newValue) ->
                validarNome(inpInstituicao, oldValue, newValue));

        inpValorDoado.textProperty().addListener((observable, oldValue, newValue) ->
                validarValor(inpValorDoado, oldValue, newValue));

        inpCpf.textProperty().addListener((observable, oldValue, newValue) ->
                validarCPF(inpCpf, oldValue, newValue));
    }

    /**
     * Método para validar os campos que contenham uma caracteristica de nome
     * @param field campo que será validado
     * @param oldValue estado antigo do campo do tipo texto (String)
     * @param newValue estado atual do campo do tipo texto (String)
     */
    private void validarNome(JFXTextField field, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            if (!newValue.matches("[A-Z a-zÀ-Ÿà-ÿ]+") || oldValue.length() != 0)
                field.setText("");
        } else if (!newValue.matches("[A-Z a-zÀ-Ÿà-ÿ]+") || newValue.length() > 50)
            field.setText(oldValue);
    }

    /**
     * Método para validar os campos que contenham uma caracteristica de valor decimal
     * @param field campo que será validado
     * @param oldValue estado antigo do campo do tipo texto (String)
     * @param newValue estado atual do campo do tipo texto (String)
     */
    private void validarValor(JFXTextField field, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            if (!newValue.matches("[0-9.,]+") || oldValue.length() != 0) {
                field.setText("");
            }
        } else if (!newValue.matches("[0-9.,]+") || newValue.length() > 50) {
            field.setText(oldValue);
        }else if (newValue.matches("[0-9]+[,.][,.]")){
            field.setText(oldValue);
        }
    }
    /**
     * Método para validar os campos que contenham uma caracteristica de CPF
     * @param field campo que será validado
     * @param oldValue estado antigo do campo do tipo texto (String)
     * @param newValue estado atual do campo do tipo texto (String)
     */
    private void validarCPF(JFXTextField field, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            if (!newValue.matches("[0-9.\\-]+") || oldValue.length() != 0)
                field.setText("");
        } else if (!newValue.matches("[0-9.\\-]+") || newValue.length() > 50)
            field.setText(oldValue);
    }
}
