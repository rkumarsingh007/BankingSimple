package dev.codescreen.Service;

import dev.codescreen.Entity.*;
import dev.codescreen.Exception.CurrencyMismatch;
import dev.codescreen.Exception.UserNotFoundException;
import dev.codescreen.Repository.AuthorizationRequestRepository;
import dev.codescreen.Repository.UserBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final UserBalanceRepository userBalanceRepository;
    private final AuthorizationRequestRepository authorizationRequestRepository;

    @Autowired
    public TransactionServiceImpl(UserBalanceRepository userBalanceRepository,
                                  AuthorizationRequestRepository authorizationRequestRepository) {
        this.userBalanceRepository = userBalanceRepository;
        this.authorizationRequestRepository = authorizationRequestRepository;
    }

    // Method to load funds
    public LoadResponse loadFunds(LoadRequest loadRequest) throws UserNotFoundException, CurrencyMismatch {
        String userId = loadRequest.getUserId();
        // Convert currency to uppercase
        loadRequest.setTransactionAmount(new TransactionAmount(
                loadRequest.getTransactionAmount().getAmount(),
                loadRequest.getTransactionAmount().getCurrency().toUpperCase(),
                loadRequest.getTransactionAmount().getDebitOrCredit()
        ));
        TransactionAmount transactionAmount = loadRequest.getTransactionAmount();
        UserBalance userBalance = userBalanceRepository.findByUserId(userId);
        if (userBalance == null) {
            loadRequest.setDescription("User ID " + userId + " not found");
            LoadResponse loadResponse = new LoadResponse(loadRequest);
            throw new UserNotFoundException(loadResponse);
        }
        if (!loadRequest.getTransactionAmount().getCurrency().equals(userBalance.getCurrency())) {
            // If currency doesn't match, throw CurrencyMismatch
            loadRequest.setDescription("Currency mismatch for user ID " + userId);
            transactionAmount.setAmount(String.valueOf(userBalance.getBalance()));
            loadRequest.setTransactionAmount(transactionAmount);
            LoadResponse loadResponse = new LoadResponse(loadRequest);
            throw new CurrencyMismatch(loadResponse);
        }
        // Handle credit and debit scenarios
        if (loadRequest.getTransactionAmount().getDebitOrCredit() == DebitCredit.CREDIT) {
            userBalance.setBalance(userBalance.getBalance() + Double.parseDouble(loadRequest.getTransactionAmount().getAmount()));
        } else if (loadRequest.getTransactionAmount().getDebitOrCredit() == DebitCredit.DEBIT) {
            double val = Double.parseDouble(loadRequest.getTransactionAmount().getAmount());
            if (userBalance.getBalance() < val) {
                // If insufficient funds, return description accordingly
                return new LoadResponse(new TransactionAmount(String.valueOf(userBalance.getBalance()),
                        transactionAmount.getCurrency(), DebitCredit.DEBIT),
                        userId, loadRequest.getMessageId(), "Insufficient funds");
            } else {
                userBalance.setBalance(userBalance.getBalance() - val);
            }
        }
        // Save updated balance and return LoadResponse
        userBalanceRepository.save(userBalance);
        return new LoadResponse(new TransactionAmount(String.valueOf(userBalance.getBalance()),
                transactionAmount.getCurrency(), loadRequest.getTransactionAmount().getDebitOrCredit()),
                userId, loadRequest.getMessageId());
    }

    // Method to authorize transaction
    public AuthorizationResponse authorizeTransaction(AuthorizationRequest authReq)
            throws UserNotFoundException, CurrencyMismatch {
        String userId = authReq.getUserId();
        // Convert currency to uppercase
        authReq.setTransactionAmount(new TransactionAmount(
                authReq.getTransactionAmount().getAmount(),
                authReq.getTransactionAmount().getCurrency().toUpperCase(),
                authReq.getTransactionAmount().getDebitOrCredit()
        ));
        TransactionAmount transactionAmount = authReq.getTransactionAmount();
        UserBalance userBalance = userBalanceRepository.findByUserId(userId);
        if (userBalance == null) {
            // If user not found, throw UserNotFoundException
            authReq.setDescription("User ID " + userId + " not found");
            AuthorizationResponse authorizationResponse = new AuthorizationResponse(authReq,ResponseCode.DECLINED);
            throw new UserNotFoundException(authorizationResponse);
        }
        if (!authReq.getTransactionAmount().getCurrency().equals(userBalance.getCurrency())) {
            // If currency doesn't match, throw CurrencyMismatch
            authReq.setDescription("Currency mismatch for user ID " + userId);
            AuthorizationResponse authorizationResponse = new AuthorizationResponse(authReq,ResponseCode.DECLINED);
            transactionAmount.setAmount(String.valueOf(userBalance.getBalance()));
            authReq.setTransactionAmount(transactionAmount);
            throw new CurrencyMismatch(authorizationResponse);
        }
        // Handle credit and debit scenarios
        if (authReq.getTransactionAmount().getDebitOrCredit() == DebitCredit.CREDIT) {
            double bal = userBalance.getBalance() + Double.parseDouble(authReq.getTransactionAmount().getAmount());
            userBalance.setBalance(bal);
        } else if (authReq.getTransactionAmount().getDebitOrCredit() == DebitCredit.DEBIT) {
            double val = Double.parseDouble(authReq.getTransactionAmount().getAmount());
            if (userBalance.getBalance() < val) {
                // If insufficient funds, return description accordingly
                TransactionAmount finalTransactionAmount = new TransactionAmount(String.valueOf(userBalance.getBalance()),
                        authReq.getTransactionAmount().getCurrency(), authReq.getTransactionAmount().getDebitOrCredit());
                AuthorizationResponse authorizationResponse =  new AuthorizationResponse(authReq.getMessageId(), userId,
                        ResponseCode.DECLINED, finalTransactionAmount,
                        "Insufficient funds");
                authorizationRequestRepository.save(authorizationResponse);
                return authorizationResponse;
            }else {
                double bal = userBalance.getBalance() - val;
                userBalance.setBalance(bal);
            }
        }
        TransactionAmount finalTransactionAmount = new TransactionAmount(String.valueOf(userBalance.getBalance()),authReq.getTransactionAmount().getCurrency(),
                authReq.getTransactionAmount().getDebitOrCredit());
        // Save updated balance and return AuthorizationResponse
        userBalanceRepository.save(userBalance);
        AuthorizationResponse authorizationResponse = new AuthorizationResponse(authReq.getMessageId(), userId,
                ResponseCode.APPROVED, finalTransactionAmount);
        authorizationRequestRepository.save(authorizationResponse);
        return authorizationResponse;
    }
}
