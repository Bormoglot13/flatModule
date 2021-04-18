package com.example.demo.dao;

import com.example.demo.model.TreeData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public interface TreeDataRepository extends CrudRepository<TreeData, Long> {

    @Query("SELECT t from TreeData t where t.root in :ids")
    Iterable<TreeData> findAllById(@Param("ids") Iterable<Long> ids);

    @Query("SELECT t from TreeData t where t.root in :ids and t.prop in :prop order by t.path")
    //@Query("SELECT t from TreeData t where t.root in :ids")
    Iterable<TreeData> findAllByIdTest(@Param("ids") Iterable<Long> ids,@Param("prop") Iterable<String> prop);

    @Query("select t from TreeData t where t.id = :#{#item.id} and t.prop = :#{#item.prop}")
    Optional<TreeData> findByRootId(TreeData item);

    @Query("select t from TreeData t where t.parentId = :#{#item.parentId} and t.prop = :#{#item.prop}")
    Iterable<TreeData> findByParentIdAndProp(TreeData item);

}
