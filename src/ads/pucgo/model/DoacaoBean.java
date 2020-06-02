package ads.pucgo.model;

public class DoacaoBean {

    private Integer id;
    private String cpfBeneficiario;
    private String nomeBeneficiario;
    private String dataDoacao;
    private String instituicaoDoadora;
    private String valorDoacao;
    private String dataEntrada;

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getCpfBeneficiario() {
        return cpfBeneficiario;
    }

    public void setCpfBeneficiario(String cpfBeneficiario) {
        this.cpfBeneficiario = cpfBeneficiario;
    }

    public String getNomeBeneficiario() {
        return nomeBeneficiario;
    }

    public void setNomeBeneficiario(String nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
    }

    public String getDataDoacao() {
        return dataDoacao;
    }

    public void setDataDoacao(String dataDoacao) {
        this.dataDoacao = dataDoacao;
    }

    public String getInstituicaoDoadora() {
        return instituicaoDoadora;
    }

    public void setInstituicaoDoadora(String instituicaoDoadora) {
        this.instituicaoDoadora = instituicaoDoadora;
    }

    public String getValorDoacao() {
        return valorDoacao;
    }

    public void setValorDoacao(String valorDoacao) {
        this.valorDoacao = valorDoacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
