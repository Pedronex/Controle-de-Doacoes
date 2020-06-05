package ads.pucgo.util;

import ads.pucgo.model.Doacao;
import ads.pucgo.model.DoacaoBean;
import ads.pucgo.service.ServiceDoacao;
import ads.pucgo.service.ServiceDoacaoImpl;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class MetodosGenericos {
    private MetodosGenericos(){

    }

    public static void carregarTabela(TableView<DoacaoBean> tabelaDoacao, TableColumn<DoacaoBean, String> clCpf, TableColumn<DoacaoBean, String> clBeneficiario, TableColumn<DoacaoBean, String> clInstituicao, TableColumn<DoacaoBean, String> clValorDoado, TableColumn<DoacaoBean, String> clDataDoacao){
        ServiceDoacao service = new ServiceDoacaoImpl();
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

    public static void mapearDoacao(DoacaoBean bean, Doacao doacao, String valorDoado, SimpleDateFormat sdf) {
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
    }

}
