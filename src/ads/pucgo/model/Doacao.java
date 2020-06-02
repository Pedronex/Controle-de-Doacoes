package ads.pucgo.model;

import ads.pucgo.util.Coluna;
import ads.pucgo.util.Tabela;

import java.sql.Date;

@Tabela(nomeTabela = "doacao")
public class Doacao {
    @Coluna(nomeColuna = "ID")
    private Integer id;
    @Coluna(nomeColuna = "CPF_BENEFICIARIO")
    private String cpf;
    @Coluna(nomeColuna = "NOME_BENEFICIARIO")
    private String nomeBeneficiario;
    @Coluna(nomeColuna = "VALOR_DOADO")
    private Double valorDoado;
    @Coluna(nomeColuna = "DATA_ENTRADA")
    private Date dataEntrada;
    @Coluna(nomeColuna = "INSTITUICAO_DOADORA")
    private String instituicaoDoadora;
    @Coluna(nomeColuna = "DATA_DOACAO")
    private Date dataDoacao;

    /**
     * @return Identificador do banco de dados
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * CPF do beneficiário
     *
     * @return cpf sem formatação do beneficiário
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Guandar CPF do beneficiário
     *
     * @param cpf CPF do beneficiário
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Nome da pessoa que recebeu o benefício
     *
     * @return beneficiário
     */
    public String getNomeBeneficiario() {
        return nomeBeneficiario;
    }

    /**
     * Guardar informação do beneficioário no objeto
     *
     * @param nomeBeneficiario nome do beneficiário
     */
    public void setNomeBeneficiario(String nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
    }

    /**
     * Valor cedido para a instituição produzir materiais para doação
     *
     * @return Doação em dinheiro
     */
    public Double getValorDoado() {
        return valorDoado;
    }

    /**
     * Guardar o valor cedido para a instituição
     *
     * @param valorDoado Valor doado para instituição
     */
    public void setValorDoado(Double valorDoado) {
        this.valorDoado = valorDoado;
    }

    /**
     * Data da baixa realizada pelo sistema
     *
     * @return Data da baixa
     */
    public Date getDataEntrada() {
        return dataEntrada;
    }

    /**
     * Armazenar a data da inserção dos dados no sistema
     *
     * @param dataEntrada Data do instante da inserção
     */
    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    /**
     * Nome da instituição que está cedendo a doação
     *
     * @return nome da instituição doadora
     */
    public String getInstituicaoDoadora() {
        return instituicaoDoadora;
    }

    /**
     * Armazenar o nome da instituição doadora
     *
     * @param instituicaoDoadora nome da instituição
     */
    public void setInstituicaoDoadora(String instituicaoDoadora) {
        this.instituicaoDoadora = instituicaoDoadora;
    }

    /**
     * Data em que o beneficiário recebeu a doação
     *
     * @return data da doação
     */
    public Date getDataDoacao() {
        return dataDoacao;
    }

    /**
     * Armazenar a data da doação
     *
     * @param dataDoacao data da doação
     */
    public void setDataDoacao(Date dataDoacao) {
        this.dataDoacao = dataDoacao;
    }
}
