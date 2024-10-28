package com.example.infopref.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.infopref.models.Enums.Prioridade;
import com.example.infopref.models.Enums.StatusOS;
import com.example.infopref.models.Enums.TipoChamado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", unique = false, nullable = false, updatable = true)
    private StatusOS status;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_chamado", unique = false, nullable = false, updatable = true)
    @NotNull
    private TipoChamado tipo_chamado;

    @Column(name = "descricao", unique = false, nullable = false, updatable = true, length = 255)
    private String descricao = "Nenhuma descrição fornecida"; // Valor padrão

    public void setDescricao(String descricao) {
        this.descricao = (descricao == null || descricao.isEmpty()) ? "Nenhuma descrição fornecida" : descricao;
    }

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "prioridade", unique = false, nullable = false, updatable = true)
    private Prioridade prioridade;

    @Column(name = "resolucao", unique = false, nullable = true, updatable = true, length = 255)
    private String resolucao = "Não definida"; // Valor padrão

    public void setResolucao(String resolucao) {
        System.out.println("Valor recebido para resolucao: " + resolucao);
        this.resolucao = (resolucao == null || resolucao.isEmpty()) ? "Não definida" : resolucao;
    }

    @Column(name = "data_abertura", unique = false, nullable = false, updatable = true)
    @NotNull
    private LocalDate data_abertura;

    @Column(name = "data_finalizacao", unique = false, nullable = true, updatable = true)
    private LocalDate data_finalizacao;

    @ManyToOne
    @JoinColumn(name = "cod_sol", nullable = false)
    private Solicitante solicitante;

    @ManyToOne
    @JoinColumn(name = "cod_tec", nullable = true)
    private Tecnico tecnico;

    @ManyToMany
    @JoinTable(name = "equip_os", joinColumns = @JoinColumn(name = "cod_os"), inverseJoinColumns = @JoinColumn(name = "cod_equip"))
    private List<Equipamento> equipamentos = new ArrayList<>();

    @Column(name = "equipamento_patrimonio", nullable = true) // Novo campo para o patrimônio não registrado
    private String equipamentoPatrimonio;

}
