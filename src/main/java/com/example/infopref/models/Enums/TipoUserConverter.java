package com.example.infopref.models.Enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoUserConverter implements AttributeConverter<TipoUser, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TipoUser attribute) {
        if (attribute == null)
            return null;

        switch (attribute) {
            case ADM -> {
                return 0;
            }

            case TECNICO -> {
                return 1;
            }

            case SOLICITANTE -> {
                return 2;
            }

            default -> throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public TipoUser convertToEntityAttribute(Integer dbData) {
        System.out.println("Convertendo valor do banco de dados para TipoUser: " + dbData);
        if (dbData == null)
            return null;
        switch (dbData) {
            case 0:
                return TipoUser.ADM;
            case 1:
                return TipoUser.TECNICO;
            case 2:
                return TipoUser.SOLICITANTE;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }

}