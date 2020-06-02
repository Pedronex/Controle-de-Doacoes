package ads.pucgo.view.controllers;

import ads.pucgo.model.Doacao;
import ads.pucgo.model.DoacaoBean;
import ads.pucgo.service.ServiceDoacao;
import ads.pucgo.service.ServiceDoacaoImpl;
import ads.pucgo.util.DoacaoException;
import ads.pucgo.util.TextFieldFormatador;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarCampos();

        btnCancelar.setOnAction(event -> {
            Parent tela;
            try {
                tela = FXMLLoader.load(getClass().getResource("../layouts/telaInicial.fxml"));
                Scene cena = btnCancelar.getScene();
                cena.setRoot(tela);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnProximo.setOnAction(event -> {
            try {
                DoacaoBean bean = setBean();
                gravarDoacao(bean);
            } catch (DoacaoException e) {
                e.printStackTrace();
            }
        });
    }

    private void gravarDoacao(DoacaoBean bean) throws DoacaoException {
        ServiceDoacao service = new ServiceDoacaoImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Doacao doacao = new Doacao();
        String valorDoado = bean.getValorDoacao().replace(",", ".");

        try {
            doacao.setDataDoacao(new Date(sdf.parse(bean.getDataDoacao()).getTime()));
            doacao.setValorDoado(Double.parseDouble(valorDoado));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        doacao.setCpf(bean.getCpfBeneficiario());
        doacao.setDataEntrada(new Date(System.currentTimeMillis()));
        doacao.setInstituicaoDoadora(bean.getInstituicaoDoadora());
        doacao.setNomeBeneficiario(bean.getNomeBeneficiario());
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
            if (!newValue.matches("[A-Z a-zÀ-Ÿà-ÿ]+") || oldValue.length() != 0)
                field.setText("");
        } else if (!newValue.matches("[A-Z a-zÀ-Ÿà-ÿ]+") || newValue.length() > 50)
            field.setText(oldValue);
    }

    private void validarValor(JFXTextField field, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            if (!newValue.matches("[0-9.,]+") || oldValue.length() != 0)
                field.setText("");
        } else if (!newValue.matches("[0-9.,]+") || newValue.length() > 50)
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
