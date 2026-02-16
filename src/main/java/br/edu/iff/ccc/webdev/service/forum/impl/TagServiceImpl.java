package br.edu.iff.ccc.webdev.service.forum.impl;

import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.repository.TagRepository;
import br.edu.iff.ccc.webdev.service.forum.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Tag> listAll() {
        return tagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getById(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + tagId));
    }
}
