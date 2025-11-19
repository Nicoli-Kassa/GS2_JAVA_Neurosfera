package com.neurosfera.repository;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoUsuario {
    APRENDIZ("Aprendiz"),
    INSTRUTOR("Instrutor"),
    CRIADOR("Criador"),
    EMPRESA("Empresa"),
    ADMINISTRADOR("Administrador");

    private String tipo;

    TipoUsuario(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    // Aceita tanto "Aprendiz" quanto "APRENDIZ" do JSON
    @JsonCreator
    public static TipoUsuario fromString(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return null;
        }

        String tipoLimpo = tipo.trim();

        // Tenta primeiro pelo valor amigável (Aprendiz, Instrutor, etc)
        for (TipoUsuario t : TipoUsuario.values()) {
            if (t.getTipo().equalsIgnoreCase(tipoLimpo)) {
                return t;
            }
        }

        // Tenta pelo nome do enum (APRENDIZ, INSTRUTOR, etc)
        try {
            return TipoUsuario.valueOf(tipoLimpo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean isValid(String tipo) {
        return fromString(tipo) != null;
    }

    public static String listarTiposValidos() {
        StringBuilder sb = new StringBuilder();
        for (TipoUsuario t : TipoUsuario.values()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(t.getTipo());
        }
        return sb.toString();
    }
}