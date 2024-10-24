package com.example.infopref.models.Enums;

public enum TipoChamado {
    HARDWARE("Hardware"),
    SOFTWARE("Software"),
    REDE("Rede"),
    SEGURANCA("Segurança"),
    SUPORTE_GERAL("Suporte Geral"),
    MANUTENCAO_PREVENTIVA("Manutenção Preventiva");

    private final String descricao;

    TipoChamado(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
