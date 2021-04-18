package com.example.demo.dao;

import com.example.demo.model.Module;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@JsonAutoDetect
@Component
public interface ModuleRepository extends CrudRepository<Module, Long>{

        @Query("SELECT m FROM Module m WHERE :date between m.created and m.updated")
        List<Module> findByDate(Date date);

        @Query("SELECT m FROM Module m WHERE m.name = :name")
        Stream<Module> findByNameReturnStream(@Param("name") String name);

        @Query("SELECT m FROM Module m WHERE m.id = :id")
        List<Module> findById(@Param("id") Integer id);
}
