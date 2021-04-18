package com.example.demo.service;

import com.example.demo.dao.CostRepository;
import com.example.demo.dao.TreeDataRepository;
import com.example.demo.json.FlatModuleBody;
import com.example.demo.model.Cost;
import com.example.demo.model.TreeData;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

@Service
//@Scope(value = "prototype")
//@RequiredArgsConstructor
@NoArgsConstructor
public class FlatModule {

    static Logger logger = LogManager.getLogger("com.ad.core");
    @Autowired
    private ApplicationContext ctxt;

    @Autowired
    private TreeDataRepository repository;

    @Autowired
    private CostRepository costRepo;

    @Value("${flatmodule.maxlevel}")
    public Integer flatModuleMaxLevel;

    @Transactional(readOnly = true)
    public List<TreeData> calculate(FlatModuleBody flatModuleBody){
        List<Long> ids = new ArrayList<>();
        ids.add(flatModuleBody.tree_id);
        List<TreeData> data = (List<TreeData>) repository.findAllById(ids);
        data = data.stream().filter(e->e.getProp().equals(flatModuleBody.prop))
                .collect(Collectors.toList());
        logger.debug("Calculate from db:" + data);
        return data;
    }


    @Transactional(readOnly = true)
    public List<Cost> getCost() {
        return (List<Cost>) costRepo.findAll();
    }
}
