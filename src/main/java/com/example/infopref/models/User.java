package com.example.infopref.models;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.infopref.models.Enums.TipoUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = User.TABLENAME)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    public static final String TABLENAME = "usuario";

    @Id
    @Column(name = "cod_usuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false, updatable = false, length = 30)
    @NotBlank
    @Size(min = 5, max = 30)
    private String username;

    @Column(name = "password", nullable = false, updatable = true)
    @NotBlank
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @CollectionTable(name = "user_profile")
    @Column(name = "profile", nullable = false)
    private Set<Integer> profiles = new HashSet<>();

    public Set<TipoUser> getProfiles() {
        return this.profiles.stream().map(x -> TipoUser.toEnum(x)).collect(Collectors.toSet());
    }

    public void addProfile(TipoUser tipoUser) {
        this.profiles.add(tipoUser.getCode());
    }
}
