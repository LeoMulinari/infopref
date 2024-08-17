package com.example.infopref.models.CompositeKeys;

import java.io.Serializable;

import lombok.Data;

@Data
public class Equip_os_Id implements Serializable {
    private Long equipId;
    private Long ordemServicoId;
}
