package br.edu.iff.ccc.webdev.service.admin.impl;

import br.edu.iff.ccc.webdev.exception.ConflictException;
import br.edu.iff.ccc.webdev.exception.NotFoundException;
import br.edu.iff.ccc.webdev.model.entity.Tag;
import br.edu.iff.ccc.webdev.repository.TagRepository;
import br.edu.iff.ccc.webdev.service.admin.AdminTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminTagServiceImpl implements AdminTagService {

    private final TagRepository tagRepository;

    @Override
    @Transactional
    public Tag create(String name) {
        if (tagRepository.existsByName(name)) {
            throw new ConflictException("A tag with this name already exists: " + name);
        }

        Tag tag = Tag.builder()
                .name(name)
                .build();

        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag update(Long tagId, String name) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NotFoundException("Tag not found with id: " + tagId));

        if (!tag.getName().equals(name) && tagRepository.existsByName(name)) {
            throw new ConflictException("A tag with this name already exists: " + name);
        }

        tag.rename(name);

        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public void delete(Long tagId) {
        if (!tagRepository.existsById(tagId)) {
            throw new NotFoundException("Tag not found with id: " + tagId);
        }

        tagRepository.deleteById(tagId);
    }
}
