package ads.pucgo.model;

public class Beneficio {

    private String produto;
    private String quantidade;
    private String unidadeMedida;

    /**
     * @return Nome do produto
     */
    public String getProduto() {
        return produto;
    }

    /**
     * @param produto Nome do produto
     */
    public void setProduto(String produto) {
        this.produto = produto;
    }

    /**
     * @return Quantidade doada
     */
    public String getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade Quantidade doada
     */
    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return Unidade de medida do produto
     */
    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    /**
     * @param unidadeMedida Unidade de medida do produto
     */
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}
