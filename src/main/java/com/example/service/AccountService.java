package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.exception.AuthenticationException;
import com.example.exception.BlankException;
import com.example.exception.RegistrationException;


@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account userRegistration(Account account) {

            if(account.getUsername().isBlank()) {
                throw new BlankException("Enter a valid username");
            }
            if(account.getPassword().length() < 4) {
                throw new IllegalArgumentException("Password must be four or more characters");
            }

            Optional<Account> realAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
            if (realAccount.isPresent()) {
                throw new RegistrationException("Account already exist");
            }
        
            return accountRepository.save(account);
    }

    public Account userLogin(Account account) {

        Account realAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
                                    .orElseThrow(() -> new AuthenticationException("Either username or password is incorrect"));
        return accountRepository.save(realAccount);
    }    
}