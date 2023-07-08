package com.example.demo.repository;

import com.example.demo.domain.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "persons", path = "persons",exported = false)
public interface PersonRepository extends JpaRepository<Person, Long> {
}
