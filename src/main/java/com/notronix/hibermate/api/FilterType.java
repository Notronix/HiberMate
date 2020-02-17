package com.notronix.hibermate.api;

public enum FilterType
{
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL("<="),
    EQUALS("="),
    GREATER_THAN_OR_EQUAL(">="),
    GREATR_THAN(">");

    private final String syntax;

    FilterType(String syntax) {
        this.syntax = syntax;
    }

    public String syntax() {
        return syntax;
    }
}
