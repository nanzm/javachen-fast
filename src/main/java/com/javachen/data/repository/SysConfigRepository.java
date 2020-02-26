package com.javachen.data.repository;

import com.javachen.data.entity.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysConfigRepository extends BaseRepository<SysConfig, Long> {
}
