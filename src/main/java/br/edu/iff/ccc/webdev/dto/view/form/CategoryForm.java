package br.edu.iff.ccc.webdev.dto.view.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryForm {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 80, message = "Nome deve ter no máximo 80 caracteres")
    private String name;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;
}
