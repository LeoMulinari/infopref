package com.example.infopref.models.Enums;

public enum StatusOS {
    EM_ABERTO("Em aberto"),
    EM_ANDAMENTO("Em andamento"),
    AGUARDANDO_PEÇAS("Aguardando peças"),
    FINALIZADO("Finalizado");

    private String displayName;

    StatusOS(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
