package com.example.demo.model;

import com.example.demo.type.LTreeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder( toBuilder = true)
@AllArgsConstructor( access = AccessLevel.PACKAGE )
@NoArgsConstructor( access = AccessLevel.PACKAGE )
@Getter
@Immutable
//@Entity
@TypeDef(name = "ltree", typeClass = LTreeType.class)
public class Component {

    private String name;
    private String prop;
    private BigDecimal value;
    @Column(name = "path", updatable = false, nullable = false, columnDefinition = "ltree")
    @Type(type = "com.example.demo.type.LTreeType")
    @JsonIgnore
    private String path;
    @JsonIgnore
    private Long root;
    @Id
    @JsonIgnore
    private Long id;
    @Column(name = "parent_id", updatable = false, nullable = false)
    @JsonIgnore
    private Long parentId;
    private List<Component> items = new ArrayList<>();

    @OneToMany(targetEntity= TreeData.class, mappedBy="college", fetch=FetchType.EAGER)
    public Component setItems(Component item) {
        items.add(item);
        item.setItems(this);
        return this;
    }


    @Override
    public String toString() {
        return "TreeData{" +
                "name='" + name + '\'' +
                ", prop='" + prop + '\'' +
                ", value=" + value +
                ", path='" + path + '\'' +
                ", root=" + root +
                '}';
    }
}
