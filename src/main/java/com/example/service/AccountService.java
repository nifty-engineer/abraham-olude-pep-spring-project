package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.exception.BlankException;
import com.example.exception.CustomDataAccessException;
import com.example.exception.ExistingAccountException;

@Service
public class AccountService {

    @Autowired(required = true)
    private AccountRepository accountRepository;

    public Account userRegistration(Account account) {

        try {
            if(account.getUsername().isBlank()) {
                throw new BlankException("Enter a valid username");
            }
            if(account.getPassword().length() < 4) {
                throw new IllegalArgumentException("Password must be four or more characters");
            }
            List<Account> allAccounts = accountRepository.findAll();
            Integer id = 0;
            for(Account acc : allAccounts) {
                if (acc.getUsername().equals(account.getUsername())) {
                    id = acc.getAccount_id();
                }
            }
            
            if (id > 0) {
                throw new ExistingAccountException("Account already exist");
            }
        
            return accountRepository.save(account);
        }
        catch (DataAccessException e) {
            new CustomDataAccessException("Account Registration is unsuccessful", e);
        }
        
        return null;
    }

    public Account userLogin(Account account) {

        Integer id = 0;

        try {
            List<Account> allAccounts = accountRepository.findAll();
            List<String> allUsernames = new ArrayList<>();
            List<String> allPasswords = new ArrayList<>();
    
            for (Account eachAccount : allAccounts) {
                allUsernames.add(eachAccount.getUsername());
                allPasswords.add(eachAccount.getPassword());

                if (eachAccount.getUsername().equals(account.getUsername()) 
                            && eachAccount.getPassword().equals(account.getPassword())) {
                    id = eachAccount.getAccount_id();
                }
            }
    
            if(!allUsernames.contains(account.getUsername()) || 
                    !allPasswords.contains(account.getPassword())) {
                throw new IllegalArgumentException("Either username or password is incorrect");
            }

            Account accountLogin = new Account(id, account.getUsername(), account.getPassword());
            return accountRepository.save(accountLogin);
        }
        catch(DataAccessException e) {
            new CustomDataAccessException("An error occurred while login in", e);
        }
        return null;
    }
    
}