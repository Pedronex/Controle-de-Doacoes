package ads.pucgo.service;

import ads.pucgo.model.Doacao;
import ads.pucgo.util.DoacaoException;

import java.util.List;

public interface ServiceDoacao {
    /**
     * Método de criação transacional para a percistencia
     * @param doacao Objeto de doação que será percistido
     * @return Resultado da execução
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    Doacao criar(Doacao doacao) throws DoacaoException;

    /**
     * Método de consulta transacional para a percistencia
     * @param id O identificador do objeto de doação que será percistido
     * @return Resultado da execução
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    Doacao consultar(Integer id) throws DoacaoException;

    /**
     * Método de alteração transacional para a percistencia
     * @param doacao Objeto de doação que será percistido
     * @return Resultado da execução
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    Doacao alterar(Doacao doacao) throws DoacaoException;

    /**
     * Método de exclusão transacionaç para a percistencia
     * @param id O identificador do objeto de doação que será percistido
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    void deletar(Integer id) throws DoacaoException;

    /**
     * Método de listagem transacional para a percistencia
     * @return Lista de Doações do banco de dados
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    List<Doacao> listar() throws DoacaoException;

}
