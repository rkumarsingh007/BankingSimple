package dev.codescreen.Repository;

import dev.codescreen.Entity.AuthorizationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRequestRepository extends JpaRepository<AuthorizationResponse, String> {
    AuthorizationResponse findByMessageId(String messageId) ;
}
