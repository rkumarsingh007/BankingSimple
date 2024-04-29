package dev.codescreen.Service;

import dev.codescreen.Entity.AuthorizationRequest;
import dev.codescreen.Entity.AuthorizationResponse;
import dev.codescreen.Entity.LoadRequest;
import dev.codescreen.Entity.LoadResponse;
import dev.codescreen.Exception.CurrencyMismatch;
import dev.codescreen.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    public LoadResponse loadFunds(LoadRequest loadRequest) throws UserNotFoundException, CurrencyMismatch;
    public AuthorizationResponse authorizeTransaction(AuthorizationRequest authorizationRequest) throws UserNotFoundException, CurrencyMismatch;


}
