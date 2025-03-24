package org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions;

import java.util.List;

public class NullEntityFieldException extends RuntimeException {
    public NullEntityFieldException(List<String> listErrors) {
        super(listErrors.toString());
    }
}
