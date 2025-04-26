package org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions;

public class UserAlreadyRegistered extends RuntimeException {

    public UserAlreadyRegistered(String cpf) {
        super("Patient already registered with CPF: " + cpf);
    }

    public UserAlreadyRegistered(String message, String email) {
        super(email + " " + message);
    }
}
