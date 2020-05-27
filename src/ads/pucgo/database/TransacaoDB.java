package ads.pucgo.database;

import java.sql.Connection;
import java.sql.DriverManager;

import ads.pucgo.util.BancoExcetion;

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

    private void conectar() throws BancoExcetion {
        try {
            conexao = DriverManager.getConnection(host, user, password);
        } catch (SQLException throwables) {
            logger.log(Level.SEVERE, "Não foi possivel realizar a conexão");
            throw new BancoExcetion(throwables);
        }
    }

    private void disconectar() throws BancoExcetion {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            throw new BancoExcetion(e);
        }
    }

    public void abrirTransacao(boolean apenasLer) throws BancoExcetion {
        try {
            if (conexao != null) {
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
