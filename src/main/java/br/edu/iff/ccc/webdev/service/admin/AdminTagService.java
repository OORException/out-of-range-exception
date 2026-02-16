package br.edu.iff.ccc.webdev.service.admin;

import br.edu.iff.ccc.webdev.model.entity.Tag;

public interface AdminTagService {
    Tag create(String name);
    Tag update(Long tagId, String name);
    void delete(Long tagId);
}
