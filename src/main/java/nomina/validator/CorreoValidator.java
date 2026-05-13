package nomina.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

/**
 * Validador personalizado para correos electrónicos.
 * Verifica que el texto ingresado cumpla con el formato estándar de un email.
 * 
 * @FacesValidator("correoValidator") permite usarlo en la vista con validator="correoValidator"
 */
@FacesValidator("correoValidator")
public class CorreoValidator implements Validator<String> {

    // Patrón Regex para validar correos
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.isEmpty()) {
            return; // El atributo 'required="true"' se encarga de los vacíos
        }

        // Si el valor no coincide con el patrón, lanza una excepción que JSF captura automáticamente
        if (!Pattern.matches(EMAIL_PATTERN, value)) {
            throw new ValidatorException(
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correo inválido", "El formato del correo no es válido (ej: usuario@dominio.com)")
            );
        }
    }
}