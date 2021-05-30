package com.project.store.formatter;

import com.project.store.domain.Tag;

import java.text.ParseException;
import java.util.Locale;

import com.project.store.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class TagFormatter implements Formatter<Tag> {

    private TagRepository tagRepository;

    @Autowired
    public TagFormatter(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public String print(Tag tag, Locale locale) {
        return tag.getName();
    }

    @Override
    public Tag parse(String name, Locale locale) throws ParseException {
        return tagRepository.findByName(name);
    }
}