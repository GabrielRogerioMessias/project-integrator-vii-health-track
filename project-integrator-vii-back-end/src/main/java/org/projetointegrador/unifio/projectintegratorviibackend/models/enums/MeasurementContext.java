package org.projetointegrador.unifio.projectintegratorviibackend.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

public enum MeasurementContext {
    JEJUM("J", "Jejum"),
    ANTES_DA_REFEICAO("PR", "Pré-prandial"),//Medição é realizada antes de qualquer refeição, mas não necessáriamente em jejum
    DURANTE_A_REFEICAO("DR", "Durante a refeição"),
    APOS_REFEICAO("AP", "Após Refeição");

    private final String code;
    @Getter
    private final String description;

    MeasurementContext(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static MeasurementContext value(String code) {
        return Arrays.stream(MeasurementContext.values())
                .filter(context -> context.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Code: " + code));
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
