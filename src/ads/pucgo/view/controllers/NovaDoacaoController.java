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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

public class NovaDoacaoController implements Initializable {

    @FXML
    private JFXTextField inpCPF;

    @FXML
    private JFXTextField inpNomeBeneficiario;

    @FXML
    private JFXDatePicker inpDataDoacao;

    @FXML
    private JFXTextField inpInstituicao;

    @FXML
    private JFXTextField inpValorDoado;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnProximo;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Alert sucesso = new Alert(Alert.AlertType.INFORMATION);

        sucesso.setTitle("Sucesso!");
        sucesso.setHeaderText("A gravação realizada");
        configurarCampos();
        btnCancelar.setOnAction(event ->
                MetodosGenericos.fechar(btnCancelar,getClass().getResource("../layouts/telaInicial.fxml")));

        btnProximo.setOnAction(event -> {
            try {
                DoacaoBean bean = setBean();
                sucesso.setContentText("Gravação realizada com sucesso a data da doação é:\n" + bean.getDataDoacao()
                        + " a data da entrada do dado é: " + sdf.format(new Date(System.currentTimeMillis())));
                gravarDoacao(bean);
                sucesso.showAndWait();
                limparCampos();
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
        });
    }

    private void limparCampos() {
        inpCPF.setText("");
        inpValorDoado.setText("");
        inpInstituicao.setText("");
        inpNomeBeneficiario.setText("");
        inpDataDoacao.getEditor().setText("");
    }

    private void gravarDoacao(DoacaoBean bean) throws DoacaoException {
        ServiceDoacao service = new ServiceDoacaoImpl();
        Doacao doacao = new Doacao();
        String valorDoado = bean.getValorDoacao().replace(",", ".");

        MetodosGenericos.mapearDoacao(bean, doacao, valorDoado, sdf);
        service.criar(doacao);
    }



    private DoacaoBean setBean() throws DoacaoException {
        DoacaoBean bean = new DoacaoBean();
        bean.setCpfBeneficiario(inpCPF.getText());
        bean.setNomeBeneficiario(inpNomeBeneficiario.getText());
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

        inpValorDoado.setText("0");
        // Validações de valores
        if (!bean.getDataDoacao().matches("[0-3][0-9]/[0-1][0-9]/[0-9]+"))
            throw new DoacaoException("O campo data não entra no formato", "A data não está no formato **/**/****");
        Calendar dataMinima = Calendar.getInstance();
        dataMinima.set(1990, Calendar.JANUARY,1);
        Date dataMaxima = new Date(System.currentTimeMillis());
        Date dataConvertida;
        try {
            dataConvertida = new Date(sdf.parse(bean.getDataDoacao()).getTime());
        } catch (ParseException e) {
            throw new DoacaoException("Não foi possivel formatar a data","Tente inserir uma data valida");
        }

        if (dataConvertida.after(dataMaxima)){
            throw new DoacaoException("Data não pode ser maior que a data atual","Tente insira uma doação antiga não no futuro");
        }else if (dataConvertida.before(dataMinima.getTime())){
            throw new DoacaoException("Data não pode ser menos que 01/01/1990","Tente inserir datas acima da data minima");
        }
        return bean;
    }

    private void configurarCampos() {
        TextFieldFormatador formatador = new TextFieldFormatador();
        formatador.setMascara("###.###.###-##");
        formatador.setCaracteresValidos("0123456789");
        // Ultilizando mascara
        inpCPF.setOnKeyReleased(event -> {
            formatador.setTexto(inpCPF);
            try {
                formatador.formatar();
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
        });

        inpNomeBeneficiario.textProperty().addListener((observable, oldValue, newValue) ->
                validarNome(inpNomeBeneficiario, oldValue, newValue));

        inpInstituicao.textProperty().addListener((observable, oldValue, newValue) ->
                validarNome(inpInstituicao, oldValue, newValue));

        inpValorDoado.textProperty().addListener((observable, oldValue, newValue) ->
                validarValor(inpValorDoado, oldValue, newValue));

        inpCPF.textProperty().addListener((observable, oldValue, newValue) ->
                validarCPF(inpCPF, oldValue, newValue));
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

    private void validarValor(JFXTextField field, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            if (!newValue.matches("[0-9.,]+") || oldValue.length() != 0)
                field.setText("");
        } else if (!newValue.matches("[0-9,]+") || newValue.length() > 50)
            field.setText(oldValue);
    }

    private void validarCPF(JFXTextField field, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            if (!newValue.matches("[0-9.\\-]+") || oldValue.length() != 0)
                field.setText("");
        } else if (!newValue.matches("[0-9.\\-]+") || newValue.length() > 50)
            field.setText(oldValue);
    }
}
