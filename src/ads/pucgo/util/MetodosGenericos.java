package ads.pucgo.util;

import ads.pucgo.model.Doacao;
import ads.pucgo.model.DoacaoBean;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class MetodosGenericos {

    private MetodosGenericos(){

    }

    /**
     * Método para voltar para na tela inicial
     * @param btn botão na tela
     * @param localizacao URL da tela inicial
     */
    public static void fechar(JFXButton btn, URL localizacao){
        Parent tela;
        try {
            tela = FXMLLoader.load(localizacao);
            Scene cena = btn.getScene();
            cena.setRoot(tela);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método de mapeamento do bean para a entidade Doação
     * @param bean classe que representa o formulario
     */
    public static Doacao mapearDoacao(DoacaoBean bean) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Doacao doacao = new Doacao();
        try {
            doacao.setDataDoacao(new Date(sdf.parse(bean.getDataDoacao()).getTime()));
            doacao.setValorDoado(Double.parseDouble(bean.getValorDoacao().replace(",",".")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        doacao.setCpf(bean.getCpfBeneficiario());
        doacao.setDataEntrada(new Date(System.currentTimeMillis()));
        doacao.setInstituicaoDoadora(bean.getInstituicaoDoadora());
        doacao.setNomeBeneficiario(bean.getNomeBeneficiario());

        return doacao;
    }

}
