package com.eddie.utils.util;

public enum Error {
    GENERIC_ERROR("E0", "Error general en back, fallo en juno o en el servlet principal"),
    INVALID_REQUEST("E1", "The request is invalid."),
    AUTHENTICATION_ERROR("E2", "Error en la autentificacion"),
    REGISTER_ERROR("E3", "Error en el registro"),
    MANDATORY_PARAMETER("E4", "Parametro obligatorio"),
    INCORRECT_PASSWORD("E5", "Contraseña incorrecta"),
    INCORRECT_EMAIL("E6", "Email incorrecto");

    private final String code;
    private final String msg;

    Error(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
