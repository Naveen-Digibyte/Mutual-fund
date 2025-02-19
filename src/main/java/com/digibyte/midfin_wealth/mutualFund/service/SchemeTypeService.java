package com.digibyte.midfin_wealth.mutualFund.service;

import com.digibyte.midfin_wealth.mutualFund.constant.ErrorConstants;
import com.digibyte.midfin_wealth.mutualFund.constant.LoggerConstants;
import com.digibyte.midfin_wealth.mutualFund.entity.SchemeType;
import com.digibyte.midfin_wealth.mutualFund.expection.SchemeTypeException;
import com.digibyte.midfin_wealth.mutualFund.repository.SchemeCategoryRepository;
import com.digibyte.midfin_wealth.mutualFund.repository.SchemeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchemeTypeService {
    
    private final SchemeTypeRepository schemeTypeRepository;

    private static final Logger logger = LoggerFactory.getLogger(SchemeTypeService.class);

    public SchemeType createSchemeType(SchemeType schemeType) {
        try {
            logger.info(LoggerConstants.LOG_019, schemeType);
            return schemeTypeRepository.save(schemeType);
        } catch (DataAccessException e) {
            logger.error(ErrorConstants.E_020, e);
            throw new SchemeTypeException(ErrorConstants.E_020, e);
        }
    }

    public List<SchemeType> getAllSchemeTypes() {
        try {
            logger.info(LoggerConstants.LOG_020);
            return schemeTypeRepository.findAll();
        } catch (DataAccessException e) {
            logger.error(ErrorConstants.E_021, e);
            throw new SchemeTypeException(ErrorConstants.E_021, e);
        }
    }

    public SchemeType getSchemeTypeById(long id) {
        try {
            logger.info(LoggerConstants.LOG_021, id);
            Optional<SchemeType> schemeType = schemeTypeRepository.findById(id);
            return schemeType.orElseThrow(() -> new SchemeTypeException(String.format(ErrorConstants.E_019, id)));
        } catch (DataAccessException e) {
            logger.error(String.format(ErrorConstants.E_022, id), e);
            throw new SchemeTypeException(String.format(ErrorConstants.E_022, id), e);
        }
    }

    public SchemeType updateSchemeType(long id, SchemeType schemeTypeDetails) {
        try {
            logger.info(LoggerConstants.LOG_022, id);
            SchemeType existingSchemeType = schemeTypeRepository.findById(id)
                    .orElseThrow(() -> new SchemeTypeException(String.format(ErrorConstants.E_019, id)));
            existingSchemeType.setType(schemeTypeDetails.getType());
            return schemeTypeRepository.save(existingSchemeType);
        } catch (DataAccessException e) {
            logger.error(String.format(ErrorConstants.E_023, id), e);
            throw new SchemeTypeException(String.format(ErrorConstants.E_023, id), e);
        }
    }

    public void deleteSchemeType(long id) {
        try {
            logger.info(LoggerConstants.LOG_023, id);
            SchemeType schemeType = schemeTypeRepository.findById(id)
                    .orElseThrow(() -> new SchemeTypeException(String.format(ErrorConstants.E_019, id)));
            schemeTypeRepository.delete(schemeType);
        } catch (DataAccessException e) {
            logger.error(String.format(ErrorConstants.E_024, id), e);
            throw new SchemeTypeException(String.format(ErrorConstants.E_024, id), e);
        }
    }
}
