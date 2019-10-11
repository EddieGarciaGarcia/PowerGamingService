package com.eddie.utils.util;

public enum Error {
    GENERIC_ERROR("E0", "Error general en back o en el servlet principal"),
    INVALID_REQUEST("E1", "The request is invalid."),
    UPDATE_FAIL("E2", "Error al actualizar"),
    SEND_FAIL("E3", "Error al enviar correo"),
    USUARIO_NOT_EXIST("E4", "No existe el usuario"),
    CREATE_FAIL("E5", "Error al crear");

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
