package com.example.demo.repository;

import com.example.demo.domain.entity.AppOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "orders",path="orders")
public interface OrderRepository extends JpaRepository<AppOrder,Long> {


}
