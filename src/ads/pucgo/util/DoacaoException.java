package ads.pucgo.util;

import javafx.scene.control.Alert;

public class DoacaoException extends Exception {
    private final Exception ex;
    private final String msg;
    private final transient Alert alert = new Alert(Alert.AlertType.ERROR);

    public DoacaoException(Exception e){
        ex = e;
        msg = e.getMessage();
        alert.setContentText(msg);
    }

    public DoacaoException(Exception e, String mensagem){
        e.printStackTrace();
        ex = e;
        msg = mensagem;
        alert.setContentText(msg);
    }

    public Exception getEx() {
        return ex;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public void printStackTrace() {
        alert.show();
    }
}
