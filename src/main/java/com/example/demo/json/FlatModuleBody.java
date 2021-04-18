package com.example.demo.json;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@JsonAutoDetect
@Component
public class FlatModuleBody {

    @Value("${flatmodule.prop}")
    @JsonIgnoreProperties
    List<String> availableStates;

    @JsonProperty("tree_id")
    @JsonPropertyDescription("Root Id")
    public Long tree_id;
    @JsonProperty("prop")
    @JsonPropertyDescription("Properties")
    public String prop;



    public Integer checkJSON() throws Exception {
        if (availableStates != null && !availableStates.contains(prop) ) {
            throw new Exception("Value \"prop\" may be only in list " + availableStates);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "FlatModuleBody{" +
                "tree_id=" + tree_id +
                ", prop='" + prop + '\'' +
                '}';
    }
}
