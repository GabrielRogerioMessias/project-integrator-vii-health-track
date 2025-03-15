package org.projetointegrador.unifio.projectintegratorviibackend.repositories;

import org.projetointegrador.unifio.projectintegratorviibackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT U FROM User U where U.email = :email")
    User findByEmail(@Param(value = "email") String email);
}
