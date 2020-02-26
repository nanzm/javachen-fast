package com.javachen.web.validation;

import com.javachen.data.entity.SysConfig;
import com.javachen.data.repository.SysConfigRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class UniqueAdValidator implements ConstraintValidator<UniqueAd, SysConfig> {

    private final SysConfigRepository sysConfigRepository;

    public UniqueAdValidator(SysConfigRepository sysConfigRepository) {
        this.sysConfigRepository = sysConfigRepository;
    }

    @Override
    public boolean isValid(SysConfig sysConfig, ConstraintValidatorContext context) {
        //TODO
        return !sysConfigRepository.existsById(sysConfig.getId());
    }

}
