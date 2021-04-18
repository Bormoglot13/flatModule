package com.example.demo.dao;

import com.example.demo.model.Cost;
import com.example.demo.model.Module;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@JsonAutoDetect
public interface CostRepository extends CrudRepository<Cost, Long>{

    @Query("SELECT c FROM Cost c WHERE c.id = :id")
    List<Cost> findById(@Param("id") Integer id);
}