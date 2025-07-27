package br.edu.ufape.plataformaeventos.util;

public enum DegreeProgram {
    CIENCIA_DA_COMPUTACAO("Ciência da Computação"),
    ADMINISTRACAO("Administração"),
    VETERINARIA("Medicina Veterinária"),
    ZOOTECNIA("Zootecnia"),
    ENGENHARIA_DE_ALIMENTOS("Engenharia de Alimentos"),
    LETRAS("Letras"),
    AGRONOMIA("Agronomia"),
    PEDAGOGIA("Pedagogia"),
    CONTABILIDADE("Ciências Contábeis");


    private final String programName;

    DegreeProgram(String programName) {
        this.programName = programName;
    }

    public String getProgramName() {
        return programName;
    }
}
