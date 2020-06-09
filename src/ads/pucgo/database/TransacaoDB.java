package ads.pucgo.database;

import ads.pucgo.util.BancoExcetion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransacaoDB {
    Logger logger;
    Connection conexao;
    String host = "jdbc:mariadb://localhost:3306/doacoes";
    String user = "root";
    String password = "123456";

    /**
     * Tentar realizar conexão com o banco de dados
     *
     * @throws BancoExcetion Classe que trata todas as exceções do banco de dados
     */
    private void conectar() throws BancoExcetion {
        try {
            conexao = DriverManager.getConnection(host, user, password);
        } catch (SQLException throwables) {
            logger.log(Level.SEVERE, "Não foi possivel realizar a conexão");
            throw new BancoExcetion(throwables);
        }
    }

    /**
     * Método para desconectar a aplicação do banco de dados
     *
     * @throws BancoExcetion Classe que trata todas as exceções do banco de dados
     */
    private void disconectar() throws BancoExcetion {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            throw new BancoExcetion(e);
        }
    }

    /**
     * Método para abrir a transção com o banco de dados
     *
     * @param apenasLer verificar se a transação será apenas para ler ou não
     * @throws BancoExcetion Classe que trata todas as exceções do banco de dados
     */
    public void abrirTransacao(boolean apenasLer) throws BancoExcetion {
        try {
            if (conexao == null) {
                conectar();
                conexao.setReadOnly(apenasLer);
            }
            if (conexao != null && !conexao.isClosed()) {
                conexao.setAutoCommit(false);
            } else {
                throw new BancoExcetion(new Exception(), "Não é possivel abrir a transação");
            }
        } catch (SQLException e) {
            throw new BancoExcetion(e);
        }
    }

    /**
     * Método para fechar transação com o banco de dados
     *
     * @throws BancoExcetion Classe que trata todas as exceções do banco de dados
     */
    public void fecharTransacao() throws BancoExcetion {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.commit();
                conexao.setAutoCommit(true);
                disconectar();
            }
        } catch (SQLException e) {
            throw new BancoExcetion(e);
        }
    }

    /**
     * Método para criar o statement para realizar as declarações
     * @return o statement para realizar as requisições
     * @throws BancoExcetion Classe que trata todas as exceções do banco de dados
     */
    public Statement createStatement() throws BancoExcetion {
        try {
            if (conexao != null && !conexao.isClosed()) {
                return conexao.createStatement();
            } else
                throw new BancoExcetion(new Exception(), "Não foi possivel criar uma transação");
        } catch (SQLException e) {
            throw new BancoExcetion(e);
        }
    }
}
