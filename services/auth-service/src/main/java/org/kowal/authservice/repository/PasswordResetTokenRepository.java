package org.kowal.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<org.kowal.authservice.entity.PasswordResetToken, String> {
}
