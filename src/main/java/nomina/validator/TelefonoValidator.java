package nomina.validator;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import java.util.regex.Pattern;

/**
 * Validador personalizado para números de teléfono.
 * Verifica que el teléfono contenga solo dígitos y tenga una longitud adecuada.
 */
@FacesValidator("telefonoValidator")
public class TelefonoValidator implements Validator<String> {

    // Valida que tenga exactamente 10 dígitos numéricos (ajusta según tu país)
    private static final String PHONE_PATTERN = "^[0-9]{10}$";

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.isEmpty()) {
            return; 
        }

        if (!Pattern.matches(PHONE_PATTERN, value)) {
            throw new ValidatorException(
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Teléfono inválido", "El teléfono debe contener exactamente 10 dígitos numéricos")
            );
        }
    }
}