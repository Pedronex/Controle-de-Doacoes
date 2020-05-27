package ads.pucgo.database;

import ads.pucgo.util.BancoExcetion;
import ads.pucgo.util.Coluna;
import ads.pucgo.util.Tabela;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenericoDAOImpl<T, ID extends Serializable> implements GenericoDAO<T, ID> {
    Logger logger;
    private final Class<T> oClass;
    protected TransacaoDB transacaoDB;

    public TransacaoDB getTransacaoDB() {
        return transacaoDB;
    }

    public void setTransacaoDB(TransacaoDB transacaoDB) {
        this.transacaoDB = transacaoDB;
    }

    @SuppressWarnings("unchecked")
    public GenericoDAOImpl() {
        oClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Class<T> getObjectClass() {
        return this.oClass;
    }

    /**
     * Inclui um objeto T no banco de dados
     * @param object objeto da classe T que será substituida
     * @return objeto incluido
     * @throws BancoExcetion classe onde será tratado todas as exceções de banco de dados
     */
    @Override
    public T incluir(T object) throws BancoExcetion {
        Statement statement = null;
        ResultSet resultSet;
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("INSERT INTO");
        sqlBuilder.append(getNomeTabela());
        sqlBuilder.append(criarInstrucaoInsert(object));
        logger.log(Level.INFO, sqlBuilder.toString());
        try {
            statement = transacaoDB.createStatement();
            statement.executeQuery(sqlBuilder.toString());
            resultSet = statement.getResultSet();
        } catch (Exception e) {
            throw new BancoExcetion(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return mapResultSetInObject(resultSet);
    }

    @Override
    public T alterar(T object) throws BancoExcetion {
        Statement statement = null;
        ResultSet resultSet;
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("UPDATE");
        sqlBuilder.append(getNomeTabela());
        sqlBuilder.append(" SET ");
        sqlBuilder.append(criarInstrucaoUpdate(object));
        sqlBuilder.append(getIdValue(object));

        try {
            statement = transacaoDB.createStatement();
            statement.execute(sqlBuilder.toString());
            resultSet = statement.getResultSet();
        } catch (Exception e) {
            throw new BancoExcetion(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return mapResultSetInObject(resultSet);
    }


    @Override
    public T consultar(Integer id) throws BancoExcetion {
        Statement statement = null;
        ResultSet resultSet;
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ");
        sqlBuilder.append(getNomeTabela());
        sqlBuilder.append(" WHERE ID = ");
        sqlBuilder.append(id);

        try {
            statement = transacaoDB.createStatement();
            statement.execute(sqlBuilder.toString());
            resultSet = statement.getResultSet();
        } catch (Exception e) {
            throw new BancoExcetion(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return mapResultSetInObject(resultSet);
    }

    @Override
    public void excluir(Integer id) throws BancoExcetion {
        Statement statement = null;
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("DELETE FROM ");
        sqlBuilder.append(getNomeTabela());
        sqlBuilder.append("WHERE ID = ");
        sqlBuilder.append(id);

        try {
            statement = transacaoDB.createStatement();
            statement.execute(sqlBuilder.toString());
        } catch (Exception e) {
            throw new BancoExcetion(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<T> listar() throws BancoExcetion {
        List<T> list = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        T objectT;

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("SELECT * FROM");
        sqlBuilder.append(getNomeTabela());
        sqlBuilder.append(" ORDER BY 1 ");
        try {

            statement = transacaoDB.createStatement();
            resultSet = statement.executeQuery(sqlBuilder.toString());

            while (resultSet != null && resultSet.next()) {
                objectT = mapResultSetInObject(resultSet);
                list.add(objectT);
            }
        } catch (Exception e) {
            throw new BancoExcetion(e);
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return list;
    }

    private String getNomeTabela() {
        return oClass.getAnnotation(Tabela.class).nomeTabela();
    }

    private String criarInstrucaoInsert(T object) {
        StringBuilder sql = new StringBuilder();
        StringBuilder campos = new StringBuilder();
        StringBuilder valores = new StringBuilder();

        try {
            campos.append("(");
            valores.append("(");

            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Coluna coluna = field.getAnnotation(Coluna.class);

                if (!"id".equalsIgnoreCase(coluna.nomeColuna())) {
                    if (campos.length() > 1) {
                        campos.append(", ");
                    }
                    if (valores.length() > 1) {
                        valores.append(", ");
                    }
                }

                campos.append(coluna.nomeColuna());
                if (isContemAspas(field.getType())) {
                    valores.append("'");
                    valores.append(field.get(object));
                    valores.append("'");
                } else {
                    valores.append(field.get(object));
                }
            }
            campos.append(")");
            valores.append(")");

            sql.append(campos);
            sql.append("VALUES");
            sql.append(valores);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sql.toString();
    }

    private String criarInstrucaoUpdate(T object) {
        StringBuilder sqlBuilder = new StringBuilder();
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Coluna coluna = field.getAnnotation(Coluna.class);

                if (sqlBuilder.length() > 1) {
                    sqlBuilder.append(", ");
                }
                sqlBuilder.append(coluna.nomeColuna());
                sqlBuilder.append(" = ");
                if (isContemAspas(field.getType())) {
                    sqlBuilder.append("'");
                    sqlBuilder.append(field.get(object));
                    sqlBuilder.append("'");
                } else {
                    sqlBuilder.append(field.get(object));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlBuilder.toString();
    }

    private T mapResultSetInObject(ResultSet resultSet) {
        T object = null;
        try {
            object = oClass.getConstructor().newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            for (Field field : oClass.getDeclaredFields()) {
                field.setAccessible(true);

                Coluna coluna = field.getAnnotation(Coluna.class);
                Object value = resultSet.getObject(coluna.nomeColuna());
                Class<?> type = field.getType();

                if (isPrimitive(type)) {
                    Class<?> boxed = mapPrimitiveClass(type);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    private String getIdValue(T object) {
        StringBuilder sqlBuilder = new StringBuilder();

        try {
            Field field = object.getClass().getDeclaredField("id");
            field.setAccessible(true);
            sqlBuilder.append(field.get(object));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sqlBuilder.toString();
    }

    private boolean isPrimitive(Class<?> type) {
        return (type == int.class || type == long.class || type == double.class || type == float.class
                || type == boolean.class || type == byte.class || type == char.class || type == short.class
                || type == Date.class);
    }

    private boolean isContemAspas(Class<?> type) {
        return type != int.class && type != long.class && type != double.class && type != float.class && type != Date.class
                && type != Integer.class && type != Long.class && type != Double.class && type != Float.class;
    }

    private Class<?> mapPrimitiveClass(Class<?> type) {
        if (int.class.equals(type)) {
            return Integer.class;
        } else if (long.class.equals(type)) {
            return Long.class;
        } else if (double.class.equals(type)) {
            return Double.class;
        } else if (float.class.equals(type)) {
            return Float.class;
        } else if (boolean.class.equals(type)) {
            return Boolean.class;
        } else if (byte.class.equals(type)) {
            return Byte.class;
        } else if (char.class.equals(type)) {
            return Character.class;
        } else if (short.class.equals(type)) {
            return Short.class;
        } else if (Date.class.equals(type)) {
            return Date.class;
        }
        String string = "Class '" + type.getName() + "' is not a primitive";
        throw new IllegalArgumentException(string);
    }
}
