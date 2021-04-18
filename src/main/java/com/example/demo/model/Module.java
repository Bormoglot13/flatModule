package com.example.demo.model;

import com.example.demo.type.LTreeType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
//import javax.persistence.Column;
import javax.persistence.*;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ModulePK.class)
@TypeDef(name = "ltree", typeClass = LTreeType.class)
public class Module {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long parent_id;
        private String name;

        @Column(name = "path", updatable = false, nullable = false, columnDefinition = "ltree")
        @Type(type = "com.example.demo.type.LTreeType")
        private String path;
        private Date created;
        private Date updated;

        @Override
        public String toString() {
            return "Module{" +
                    "id=" + id +
                    ", parent_id='" + parent_id + '\'' +
                    ", name='" + name + '\'' +
                    ", created='" + created + '\'' +
                    ", updated='" + updated +
                    '}';
        }
}
