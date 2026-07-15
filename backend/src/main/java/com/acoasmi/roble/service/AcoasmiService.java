package com.acoasmi.roble.service;

import java.util.List;

public interface AcoasmiService <E, REQ, RES, ID> {

    List<RES> getAll();
    RES getById(ID id);
    RES create(REQ requestDto);
    RES update(ID id, REQ requestDto);
    void delete(ID id);
    void cambiarEstado(ID id, Boolean estado);
}
