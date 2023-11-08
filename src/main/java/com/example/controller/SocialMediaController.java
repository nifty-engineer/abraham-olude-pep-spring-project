package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.exception.RegistrationException;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> userRegistration(@RequestBody Account account) {
        
        try {
            Account registeredAccount = accountService.userRegistration(account);
            return ResponseEntity.status(200)
                            .body(registeredAccount);
        }
        catch (RegistrationException e) {
            e.printStackTrace();
            return ResponseEntity.status(409)
                    .body(account);
        }
        catch (Exception e) {
            e.printStackTrace();        
            return ResponseEntity.status(400)
                    .body(account);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> userLogin(@RequestBody Account account) {

        try {
            Account accountLogin = accountService.userLogin(account);
            return ResponseEntity.status(200)
                        .body(accountLogin);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401)
                        .body(account);
        }
    }


    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        
        try {
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.status(200)
                            .body(createdMessage);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400)
                            .body(message);
        }
    }
    
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> retrieveAllMessages() {

        try {
            List<Message> messages = messageService.retrieveAllMessages();
            return ResponseEntity.status(200)
                        .body(messages);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(200)
                        .build();
        }
    }
    
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> retrieveMessageByMessageId(@PathVariable String message_id) {

        try {
            Message message = messageService.retrieveMessageByMessageId(Integer.valueOf(message_id));
            return ResponseEntity.status(200)
                        .body(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(200)
                        .build();
        }
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageByMessageId(@PathVariable String message_id) {

        try {
            int rowsAffected = messageService.deleteMessageByMessageId(Integer.valueOf(message_id));
            return ResponseEntity.status(200)
                        .body(rowsAffected);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(200)
                        .build();
        }
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable String message_id, @RequestBody Message newMessage) {

        try {
            int rowsAffected = messageService.updateMessage(Integer.valueOf(message_id), newMessage);
            return ResponseEntity.status(200)
                        .body(rowsAffected);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400)
                        .build();
        }
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> retrieveAllMessagesForUser(@PathVariable String account_id) {

        try {
            List<Message> messages = messageService.retrieveAllMessagesForUser(Integer.valueOf(account_id));
            return ResponseEntity.status(200)
                        .body(messages);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(200)
                        .build();
        }
    }    
}