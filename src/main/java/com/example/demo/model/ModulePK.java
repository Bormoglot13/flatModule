package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@RequiredArgsConstructor
public class ModulePK implements Serializable {

    protected Long id;
    protected Long parent_id;
}
