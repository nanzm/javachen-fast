package com.javachen.business.service.impl;

import com.javachen.business.service.AbstractBaseSerivce;
import com.javachen.business.service.SysLogService;
import com.javachen.data.repository.BaseRepository;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl<SysLog, Long> extends AbstractBaseSerivce implements SysLogService {
    @Override
    public BaseRepository getRepository() {
        return null;
    }
}
