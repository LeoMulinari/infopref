package com.example.infopref.models;

import java.sql.Date;

import com.example.infopref.models.Enums.Prioridade;
import com.example.infopref.models.Enums.StatusOS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = OrdemServico.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrdemServico {
    public static final String TABLENAME = "ordemServico";

    @Id
    @Column(name = "cod_os")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_protocolo", unique = true, nullable = false, updatable = false, length = 20) // rever update
    @NotBlank
    private String num_protocolo;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", unique = false, nullable = false, updatable = true)
    private StatusOS status;

    @Column(name = "tipo_chamado", unique = false, nullable = false, updatable = true, length = 100)
    @NotBlank
    private String tipo_chamado;

    @Column(name = "descricao", unique = false, nullable = false, updatable = true, length = 255)
    private String descricao = "Nenhuma descrição fornecida"; // Valor padrão

    public void setDescricao(String descricao) {
        this.descricao = (descricao == null || descricao.isEmpty()) ? "Nenhuma descrição fornecida" : descricao;
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "prioridade", unique = false, nullable = false, updatable = true)
    private Prioridade prioridade;

    @Column(name = "resolucao", unique = false, nullable = false, updatable = true, length = 255)
    private String resolucao = "Não definida"; // Valor padrão

    public void setResolucao(String resolucao) {
        this.resolucao = (resolucao == null || resolucao.isEmpty()) ? "Não definida" : resolucao;
    }

    @Column(name = "data_abertura", unique = false, nullable = false, updatable = true)
    @NotNull
    private Date data_abertura;

    @Column(name = "data_finalizacao", unique = false, nullable = false, updatable = true)
    @NotNull
    @FutureOrPresent
    private Date data_finalizacao;

    @ManyToOne
    @JoinColumn(name = "cod_sol", nullable = false)
    private Solicitante solicitante;

    @ManyToOne
    @JoinColumn(name = "cod_tec", nullable = false)
    private Tecnico tecnico;

    @ManyToOne
    @JoinColumn(name = "cod_usuario", nullable = false)
    private User user;

}
