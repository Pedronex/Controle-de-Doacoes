package ads.pucgo.database;

import ads.pucgo.util.BancoExcetion;

import java.io.Serializable;
import java.util.List;

public interface GenericoDAO<T,ID extends Serializable> {

    /**
     * @return classe a ser percistida
     */
    Class<T> getObjectClass();

    /**
     * Operação de inclusão do objeto no banco de dados
     * @param object objeto da classe T que será substituida
     * @return objeto T que foi incluida no banco
     * @throws BancoExcetion classe onde será tratado todas as exceções de banco de dados
     */
    T incluir(T object) throws BancoExcetion;

    /**
     * Operação de alteração do objeto no banco de dados
     * @param object objeto da classe T que será substituida
     * @return objeto T que foi alterado no banco
     * @throws BancoExcetion classe onde será tratado todas as exceções de banco de dados
     */
    T alterar(T object) throws BancoExcetion;

    /**
     * Operação de consulta do objeto no banco de dados
     * @param id identificador do objeto
     * @return Objeto T que foi consultado
     * @throws BancoExcetion classe onde será tratado todas as exceções de banco de dados
     */
    T consultar(Integer id) throws BancoExcetion;

    /**
     * Operação de exclusão do objeto no banco de dados
     * @param id identificador do objeto
     * @throws BancoExcetion classe onde será tratado todas as exceções de banco de dados
     */
    void excluir(Integer id) throws BancoExcetion;

    /**
     * Operação de listagem de todos os objetos no banco de dados
     * @return listagem de todos os objetos do banco de dados
     * @throws BancoExcetion classe onde será tratado todas as exceções de banco de dados
     */
    List<T> listar() throws BancoExcetion;

    /**
     * Método para setar a transação no banco de dados
     * @param transacaoDB Transição do banco de dados
     */
    void setTransacaoDB(TransacaoDB transacaoDB);

    /**
     * @return Objeto de transação
     */
    TransacaoDB getTransacaoDB();
}
