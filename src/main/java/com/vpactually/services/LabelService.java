package com.vpactually.services;

import com.vpactually.dao.LabelDAO;
import com.vpactually.dto.labels.LabelCreateUpdateDTO;
import com.vpactually.dto.labels.LabelDTO;
import com.vpactually.mappers.LabelMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

public class LabelService {

    private static final LabelService INSTANCE = new LabelService();

    private static final LabelDAO LABEL_DAO = LabelDAO.getInstance();
    private static final LabelMapper LABEL_MAPPER = Mappers.getMapper(LabelMapper.class);

    private LabelService() {
    }

    public static LabelService getInstance() {
        return INSTANCE;
    }

    public List<LabelDTO> findAll() {
        return LABEL_DAO.findAll().stream().map(LABEL_MAPPER::map).toList();
    }

    public Optional<LabelDTO> findById(Integer id) {
        return LABEL_DAO.findById(id).map(LABEL_MAPPER::map);
    }

    public LabelDTO update(LabelCreateUpdateDTO labelDTO, Integer id) {
        var label = LABEL_DAO.findById(id).orElseThrow();
        LABEL_MAPPER.update(labelDTO, label);
        LABEL_DAO.save(label);
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
