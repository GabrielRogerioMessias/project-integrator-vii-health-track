package org.projetointegrador.unifio.projectintegratorviibackend.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> entityName, String idEntity) {
        super(entityName.getSimpleName() + " not found with id: " + idEntity);
    }
}
