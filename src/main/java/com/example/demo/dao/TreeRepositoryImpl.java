package com.example.demo.dao;

import com.example.demo.model.TreeData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TreeRepositoryImpl implements TreeRepository {

    final @NonNull EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<TreeData> findNestedItems(TreeData treeData) {
        List<TreeData> list = entityManager
                .createQuery("SELECT t from TreeData t where t.parentId = :parentId and t.prop = :prop")
                .setParameter("parentId", treeData.getParentId())
                .setParameter("prop", treeData.getProp())
                .getResultList();
        //list.stream().forEach(t->t.setItems(treeData));
        return list;
    }
}