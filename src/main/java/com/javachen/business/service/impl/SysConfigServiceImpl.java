package com.javachen.business.service.impl;

import com.javachen.business.service.AbstractBaseSerivce;
import com.javachen.business.service.SysConfigService;
import com.javachen.data.repository.BaseRepository;
import com.javachen.data.repository.SysConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigServiceImpl<SysConfig, Long> extends AbstractBaseSerivce implements SysConfigService {
    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Override
    public BaseRepository getRepository() {
        return sysConfigRepository;
    }
}
