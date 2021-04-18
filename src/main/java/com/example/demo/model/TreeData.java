package com.example.demo.model;

import com.example.demo.type.LTreeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SortComparator;
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
//@RequiredArgsConstructor
//@Embeddable
@Entity(name = "TreeData")
@Table(name = "v_tree_data")
//@Immutable
@Access(AccessType.FIELD)
@TypeDef(name = "ltree", typeClass = LTreeType.class)
public class TreeData {

    @Column(name = "name", updatable = false, nullable = false)
    private String name;
    @Column(name = "prop", updatable = false, nullable = false)
    private String prop;
    @Column(name = "value", updatable = false, nullable = false)
    private BigDecimal value;
    @Column(name = "path", updatable = false, nullable = false, columnDefinition = "ltree")
    @Type(type = "com.example.demo.type.LTreeType")
    @JsonIgnore
    private String path;
    @Column(name = "root", updatable = false, nullable = false)
    @JsonIgnore
    private Long root;
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @JsonIgnore
    private Long id;
    @Column(name = "parent_id", updatable = false, nullable = false)
    @JsonIgnore
    private Long parentId;

    //TODO implement nested items
//    @OneToMany(mappedBy = "parent_id", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ManyToOne
//    @JoinColumn(name="id")
//    @Setter
//    private Component items;


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
