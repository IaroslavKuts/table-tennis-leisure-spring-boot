package com.example.demo.repository;

import com.example.demo.domain.entity.Abonement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource
public interface AbonementRepository extends JpaRepository<Abonement, Long>{

}
