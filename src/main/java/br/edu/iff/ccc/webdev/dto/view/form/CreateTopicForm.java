package br.edu.iff.ccc.webdev.dto.view.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreateTopicForm {

    @NotNull(message = "Categoria é obrigatória")
    private Long categoryId;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 140, message = "Título deve ter no máximo 140 caracteres")
    private String title;

    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
    private String description;

    private Set<Long> tagIds = new HashSet<>();
}
