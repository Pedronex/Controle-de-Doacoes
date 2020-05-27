package ads.pucgo.service;

import ads.pucgo.database.DoacaoDAO;
import ads.pucgo.database.DoacaoDAOImpl;
import ads.pucgo.database.TransacaoDB;
import ads.pucgo.model.Doacao;
import ads.pucgo.util.BancoExcetion;
import ads.pucgo.util.DoacaoException;

import java.util.List;

public class ServiceDoacaoImpl implements ServiceDoacao{

    private DoacaoDAO dao;

    public DoacaoDAO getDao() {
        return dao;
    }

    /**
     * Método de validação de criação
     * @param doacao Objeto de doação que será percistido
     * @return Objeto criado
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    @Override
    public Doacao criar(Doacao doacao) throws DoacaoException {
        dao = new DoacaoDAOImpl();
        Doacao objeto;
        getDao().setTransacaoDB(new TransacaoDB());

        try {
            getDao().getTransacaoDB().abrirTransacao(false);
            objeto = getDao().incluir(doacao);
            getDao().getTransacaoDB().fecharTransacao();
        } catch (BancoExcetion bancoExcetion) {
            throw new DoacaoException(bancoExcetion);
        }
        return objeto;
    }

    /**
     * Método de validação de consulta
     * @param id O identificador do objeto de doação que será percistido
     * @return Objeto que foi consultado
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    @Override
    public Doacao consultar(Integer id) throws DoacaoException {
        dao = new DoacaoDAOImpl();
        Doacao objeto;
        getDao().setTransacaoDB(new TransacaoDB());

        try {
            getDao().getTransacaoDB().abrirTransacao(false);
            objeto = getDao().consultar(id);
            getDao().getTransacaoDB().fecharTransacao();
        } catch (BancoExcetion bancoExcetion) {
            throw new DoacaoException(bancoExcetion);
        }
        return objeto;
    }

    /**
     * Método de validação de alteração
     * @param doacao Objeto de doação que será percistido
     * @return Objeto que foi alterado
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    @Override
    public Doacao alterar(Doacao doacao) throws DoacaoException {
        dao = new DoacaoDAOImpl();
        Doacao objeto;
        getDao().setTransacaoDB(new TransacaoDB());

        try {
            getDao().getTransacaoDB().abrirTransacao(false);
            objeto = getDao().alterar(doacao);
            getDao().getTransacaoDB().fecharTransacao();
        } catch (BancoExcetion bancoExcetion) {
            throw new DoacaoException(bancoExcetion);
        }
        return objeto;
    }

    /**
     * Método de validação de deleção
     * @param id O identificador do objeto de doação que será percistido
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    @Override
    public void deletar(Integer id) throws DoacaoException {
        dao = new DoacaoDAOImpl();

        getDao().setTransacaoDB(new TransacaoDB());

        try {
            getDao().getTransacaoDB().abrirTransacao(false);
            getDao().excluir(id);
            getDao().getTransacaoDB().fecharTransacao();
        } catch (BancoExcetion bancoExcetion) {
            throw new DoacaoException(bancoExcetion);
        }
    }

    /**
     * Método de chamada de listagem na percistencia
     * @return Lista de doações - {@link Doacao}
     * @throws DoacaoException Classe onde será tratado as exceções do usuário
     */
    @Override
    public List<Doacao> listar() throws DoacaoException {
        dao = new DoacaoDAOImpl();
        List<Doacao> objeto;
        getDao().setTransacaoDB(new TransacaoDB());

        try {
            getDao().getTransacaoDB().abrirTransacao(false);
            objeto = getDao().listar();
            getDao().getTransacaoDB().fecharTransacao();
        } catch (BancoExcetion bancoExcetion) {
            throw new DoacaoException(bancoExcetion);
        }
        return objeto;
    }
}
