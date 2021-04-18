package com.example.demo.dao;

import com.example.demo.model.Component;
import com.example.demo.model.TreeData;

import java.util.List;

public interface TreeRepository {

    List<TreeData> findNestedItems(TreeData treeData);
}
