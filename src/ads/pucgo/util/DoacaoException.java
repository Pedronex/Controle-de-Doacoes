package ads.pucgo.util;

import javafx.scene.control.Alert;

public class DoacaoException extends Exception {
    private final String msg;
    private final transient Alert alert;

    public DoacaoException(Exception e){
        alert = new Alert(Alert.AlertType.ERROR);
        msg = e.getMessage();
        alert.setContentText(msg);
    }

    public DoacaoException(String msg, String solucao){
        alert = new Alert(Alert.AlertType.ERROR);
        this.msg = msg;
        alert.setHeaderText(msg);
        alert.setContentText(solucao);
    }

    @Override
    public void printStackTrace() {
        alert.show();
    }
}
