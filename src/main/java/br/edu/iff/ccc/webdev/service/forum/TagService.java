package br.edu.iff.ccc.webdev.service.forum;

import br.edu.iff.ccc.webdev.model.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> listAll();
    Tag getById(Long tagId);
}
