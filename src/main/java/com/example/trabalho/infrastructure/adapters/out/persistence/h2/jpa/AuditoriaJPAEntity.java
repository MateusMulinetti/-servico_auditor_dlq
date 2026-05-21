package com.example.trabalho.infrastructure.adapters.out.persistence.h2.jpa;

import com.example.trabalho.infrastructure.adapters.out.persistence.h2.entity.AuditoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaJPAEntity extends JpaRepository<AuditoriaEntity, String> {
}