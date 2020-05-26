package ads.pucgo.util;

public class BancoExcetion extends Exception {
    private final Exception ex;
    private final String msg;

    public BancoExcetion(Exception e){
        ex = e;
        msg = e.getMessage();
    }

    public BancoExcetion(Exception e, String mensagem){
        e.printStackTrace();
        ex = e;
        msg = mensagem;
    }

    public Exception getEx() {
        return ex;
    }

    public String getMsg() {
        return msg;
    }

}
