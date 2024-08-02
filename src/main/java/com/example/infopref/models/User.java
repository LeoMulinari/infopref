package com.example.infopref.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name=User.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    public static final String TABLENAME = "usuario";

    @Id
    @Column(name="cod_usuario")
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(name="login", unique=true, nullable=false, updatable=false)
    @NotBlank
    @Size(min=5, max=30)
    private String username;

    @Column(name="password", nullable=false, updatable=true)
    @NotBlank
    @Size(min=8, max=20)
    @JsonProperty(access= Access.WRITE_ONLY)
    private String password;
}
