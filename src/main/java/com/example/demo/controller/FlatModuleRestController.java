package com.example.demo.controller;

import com.example.demo.json.FlatModuleBody;
import com.example.demo.model.Cost;
import com.example.demo.model.TreeData;
import com.example.demo.service.FlatModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@SpringBootConfiguration
public class FlatModuleRestController {

    static Logger logger = LogManager.getLogger("com.ad.core");

    @Autowired
    private FlatModule flatModule;
    @Autowired
    private BeanFactory beanFactory;

    
    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "Hello";
    }

    @RequestMapping( value = "/calculate", method = { RequestMethod.POST })
    @ResponseBody
    public List<TreeData> calculate(@RequestBody(required=true) ObjectNode objectNode) throws Exception {
        return flatModule.calculate(calculateCheck(objectNode));
    }

    @RequestMapping( value = "/cost", method = { RequestMethod.POST })
    @ResponseBody
    public List<Cost> getCost(@RequestBody(required=true) ObjectNode objectNode) throws Exception {
        return flatModule.getCost();
    }

    public FlatModuleBody calculateCheck(ObjectNode objectNode) throws Exception {
        FlatModuleBody flatModuleBody = getBody(objectNode);
        if (flatModuleBody != null) {
            flatModuleBody.checkJSON();
        }
        return flatModuleBody;
    }

    public static FlatModuleBody getBody(ObjectNode objectNode) throws IOException {
        FlatModuleBody flatModuleBody = null;
        if (objectNode != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode bodyNode = mapper.readTree(objectNode.toString());
            byte[] json = mapper.writeValueAsBytes(bodyNode);
            flatModuleBody = mapper.readValue(json, FlatModuleBody.class);
        }
        return flatModuleBody;
    }

}
