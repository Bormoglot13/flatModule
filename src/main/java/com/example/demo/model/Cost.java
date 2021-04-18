package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cost {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PURCHASE_COST", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal purchaseCost;
    @Column(name = "DELIVERY_COST", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal deliveryCost;
    @Column(name = "INSTALL_COST", columnDefinition = "NUMERIC(19,2)")
    private BigDecimal installCost;
    private Date created;
    private Date updated;

    public BigDecimal getCost() {
        return purchaseCost.add(deliveryCost).add(installCost);
    }

    @Override
    public String toString() {
        return "Cost{" +
                "id=" + id +
                ", purchaseCost=" + purchaseCost +
                ", deliveryCost=" + deliveryCost +
                ", installCost=" + installCost +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
