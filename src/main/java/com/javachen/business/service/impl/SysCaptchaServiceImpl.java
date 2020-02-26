package com.javachen.business.service.impl;

import com.google.code.kaptcha.Producer;
import com.javachen.business.service.SysCaptchaService;
import com.javachen.common.exception.BusinessException;
import com.javachen.common.utils.DateUtils;
import com.javachen.data.entity.SysCaptcha;
import com.javachen.data.repository.SysCaptchaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.image.BufferedImage;
import java.util.Date;

@Service
public class SysCaptchaServiceImpl implements SysCaptchaService {
    @Autowired
    private Producer producer;

    @Autowired
    private SysCaptchaRepository sysCaptchaRepository;

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if (StringUtils.isEmpty(uuid)) {
            throw new BusinessException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();

        SysCaptcha captchaEntity = new SysCaptcha();
        captchaEntity.setId(uuid);
        captchaEntity.setCode(code);
        //5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        save(captchaEntity);

        return producer.createImage(code);
    }

    @Override
    public boolean validate(String id, String code) {
        SysCaptcha captchaEntity = this.findById(id);
        if (captchaEntity == null) {
            return false;
        }

        //删除验证码
        this.delete(id);

        if (captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()) {
            return true;
        }

        return false;
    }

    @Override
    public void save(SysCaptcha sysCaptcha) {
        sysCaptchaRepository.save(sysCaptcha);
    }

    @Override
    public void delete(String id) {
        sysCaptchaRepository.deleteById(id);
    }

    @Override
    public SysCaptcha findById(String id) {
        return sysCaptchaRepository.findById(id).orElse(null);
    }
}
