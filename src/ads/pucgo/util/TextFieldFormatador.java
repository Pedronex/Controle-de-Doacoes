package ads.pucgo.util;

import javafx.scene.control.TextField;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class TextFieldFormatador {

    private final MaskFormatter formatador;
    private TextField texto;
    private String caracteresValidos;
    private String mascara;

    public TextFieldFormatador() {
        formatador = new MaskFormatter();
    }

    public void setTexto(TextField texto) {
        this.texto = texto;
    }

    public void setCaracteresValidos(String caracteresValidos) {
        this.caracteresValidos = caracteresValidos;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    /**
     * Este método realiza a conversão dos dados inseridos
     * @param campo {@link TextField}
     * @param caracteresValidos Conjuto de caracteres que pode ser escrito no texto
     * @param mascara Mascara que pode ser ultilizada no {@link MaskFormatter}
     * @throws DoacaoException Exceção de conversão ou de dados validos
     */
    public void formatar(TextField campo, String caracteresValidos, String mascara) throws DoacaoException {
        try {
            // Inserindo a mascara no formatador
            formatador.setMask(mascara);
        } catch (ParseException ex) {
            throw new DoacaoException(ex);
        }
        // inserindo caracteris validos
        formatador.setValidCharacters(caracteresValidos);
        // Apresentar os caracteres da mascara
        formatador.setValueContainsLiteralCharacters(false);
        // retirando os espaços e os caracteres especias do texto
        String text = campo.getText().replaceAll("[\\W]", "");

        boolean valido = true;
        while (valido) {
            // Validar se o caractere está vazio
            if (text.isEmpty()) {
                break;
            }
            // Tentar formatar o texto
            try {
                text = formatador.valueToString(text);
                valido = false;
            } catch (ParseException ex) {
                throw new DoacaoException("Valores não permitidos","Este campo só pode conter esses valores: " + caracteresValidos);
            }
        }
        // Mandar para o TextField o texto formatado
        campo.setText(text);
        // Deixando o ponteiro no final do campo
        if (!text.isEmpty()) {
            for (int i = 0; campo.getText().charAt(i) != ' ' && i < campo.getLength() - 1; i++) {
                campo.forward();
            }
            campo.forward();
        }
    }
    public void formatar() throws DoacaoException {
        formatar(this.texto, this.caracteresValidos, this.mascara);
    }

}