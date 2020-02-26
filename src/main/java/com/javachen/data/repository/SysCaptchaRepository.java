package com.javachen.data.repository;

import com.javachen.data.entity.SysCaptcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysCaptchaRepository extends JpaRepository<SysCaptcha, String> {
}
