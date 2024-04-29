package dev.codescreen.controller;

import dev.codescreen.Entity.*;
import dev.codescreen.Exception.CurrencyMismatch;
import dev.codescreen.Exception.UserNotFoundException;
import dev.codescreen.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/load/{messageId}")
    public ResponseEntity<LoadResponse> loadFunds(@PathVariable String messageId, @RequestBody LoadRequest loadRequest) throws UserNotFoundException, CurrencyMismatch {
        LoadResponse loadResponse = transactionService.loadFunds(loadRequest);
        return new ResponseEntity<>(loadResponse, HttpStatus.CREATED);
    }

    @PutMapping("/authorization/{messageId}")
    public ResponseEntity<AuthorizationResponse> authorizeTransaction(@PathVariable String messageId, @RequestBody AuthorizationRequest authorizationRequest) throws UserNotFoundException, CurrencyMismatch {
        AuthorizationResponse authorizationResponse = transactionService.authorizeTransaction(authorizationRequest);
        return new ResponseEntity<>(authorizationResponse, HttpStatus.CREATED);
    }
}