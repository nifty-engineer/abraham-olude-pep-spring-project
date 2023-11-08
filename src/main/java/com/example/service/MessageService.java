package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.BlankException;
import com.example.exception.ExcessiveCharactersException;
import com.example.exception.MessageCreationException;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;


@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        
        String text = message.getMessage_text();
        if (text.isBlank()) {
            throw new BlankException("Enter a valid message");
        }
        if (text.length() > 254) {
            throw new ExcessiveCharactersException("Message is too long");
        }
        
        accountRepository.findById(message.getPosted_by())
                    .orElseThrow(() -> new MessageCreationException("Message is not from a registered account"));

        return messageRepository.save(message);            
    }

    public List<Message> retrieveAllMessages() {

        return messageRepository.findAll();
    }

    public Message retrieveMessageByMessageId(Integer messageId) {

        return messageRepository.findById(messageId).orElseGet(() -> null);
    }

    public int deleteMessageByMessageId(Integer messageId) {

        int rowsAffected = retrieveMessageByMessageId(messageId) == null ? 0 : 1;

        if (rowsAffected == 1) {
            messageRepository.deleteById(messageId);
        }

        return rowsAffected;
    }

    public int updateMessage(Integer messageId, Message newMessage) {

        Message oldMessage = messageRepository.findById(messageId).orElseGet(() -> null);

        int rowsAffected = oldMessage == null ? 0 : 1;

        if (rowsAffected == 1) {
            messageRepository.deleteById(messageId);
        }
        else {
            throw new ResourceNotFoundException("No prior message available");
        }

        String text = newMessage.getMessage_text();
        if (text.isBlank()) {
            throw new BlankException("Enter a valid message");
        }
        if (text.length() > 254) {
            throw new ExcessiveCharactersException("Message is too long");
        }
        messageRepository.save(newMessage);

        return rowsAffected;
    }

    public List<Message> retrieveAllMessagesForUser(Integer accountId) {

        List<Integer> user = new ArrayList<>();
        user.add(accountId);
        return messageRepository.findAllById(user);
    }
    
}