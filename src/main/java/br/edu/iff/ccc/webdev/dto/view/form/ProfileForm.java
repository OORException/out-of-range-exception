package br.edu.iff.ccc.webdev.dto.view.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileForm {

    @Size(min = 3, max = 50, message = "Nome de usuário deve ter entre 3 e 50 caracteres")
    private String username;

    @Size(min = 3, max = 100, message = "Nome completo deve ter entre 3 e 100 caracteres")
    private String fullName;

    @Email(message = "Informe um e-mail válido")
    private String email;
}
