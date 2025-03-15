package org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions;

public class UserAlreadyRegistered extends RuntimeException {

    public UserAlreadyRegistered(Class<?> className, String message) {
        super(className.getSimpleName() + " already registered with e-mail: " + message);
    }
}
