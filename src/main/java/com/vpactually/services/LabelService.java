package com.vpactually.services;

import com.vpactually.repositories.LabelRepository;
import com.vpactually.dto.labels.LabelCreateUpdateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.mappers.LabelMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository LABEL_DAO;
    private final LabelMapper LABEL_MAPPER;

    public List<LabelDTO> findAll() {
        return LABEL_DAO.findAll().stream().map(LABEL_MAPPER::map).toList();
    }

    public Optional<LabelDTO> findById(Integer id) {
        return LABEL_DAO.findById(id).map(LABEL_MAPPER::map);
    }

    public LabelDTO update(LabelCreateUpdateDTO labelDTO, Integer id) {
        var label = LABEL_DAO.findById(id).orElseThrow();
        LABEL_MAPPER.update(labelDTO, label);
        LABEL_DAO.update(label);
        return LABEL_MAPPER.map(label);
    }

    public LabelDTO save(LabelCreateUpdateDTO labelDTO) {
        var label = LABEL_MAPPER.map(labelDTO);
        LABEL_DAO.save(label);
        return LABEL_MAPPER.map(label);
    }

    public void deleteById(Integer id) {
        LABEL_DAO.deleteById(id);
    }


}
