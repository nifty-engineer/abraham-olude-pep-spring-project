package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.BlankException;
import com.example.exception.CustomDataAccessException;
import com.example.exception.ExcessiveCharactersException;
import com.example.exception.NonExistentUser;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired(required = true)
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        
        try {
            String text = message.getMessage_text();
            if (text.isBlank()) {
                throw new BlankException("Enter a valid message");
            }
            if (text.length() > 254) {
                throw new ExcessiveCharactersException("Message is too long");
            }
            List<Message> allMessages = messageRepository.findAll();
            List<Integer> allPostedBys = new ArrayList<>();
            for(Message msg : allMessages) {
                allPostedBys.add(msg.getPosted_by());
            }
            if (!allPostedBys.contains(message.getPosted_by())) {
                throw new NonExistentUser("Message is not from a registered account");
            }

            return messageRepository.save(message);            
        }
        catch(DataAccessException e) {
            new CustomDataAccessException("Something went wrong", e);
        }
        
        return null;
    }

    public List<Message> retrieveAllMessages() {

        try {
            return messageRepository.findAll();
        }
        catch(DataAccessException e) {
            new CustomDataAccessException("Something went wrong", e);
        }

        return new ArrayList<>();
    }

    public Message retrieveMessageByMessageId(Integer messageId) {

        try {
            return messageRepository.findById(messageId).orElseThrow(NoSuchElementException:: new);
        }
        catch(DataAccessException e) {
            new CustomDataAccessException("Something went wrong", e);
        }

        return null;
    }

    public int deleteMessageByMessageId(Integer messageId) {

        int rowsAffected = 0;
        try {
            rowsAffected = retrieveMessageByMessageId(messageId) == null ? 0 : 1;

            messageRepository.deleteById(messageId);
        }
        catch(DataAccessException e) {
            new CustomDataAccessException("Something went wrong", e);
        }

        return rowsAffected;
    }

    public int updateMessage(Integer messageId, Message newMessage) {

        int rowsAffected = 0;
        try {
            Message oldMessage = retrieveMessageByMessageId(messageId);
            rowsAffected = oldMessage == null ? 0 : 1;

            if (rowsAffected == 1) {
                messageRepository.deleteById(oldMessage.getMessage_id());
            }
            else {
                throw new NoSuchElementException("No prior message exists");
            }

            String text = newMessage.getMessage_text();
            if (text.isBlank()) {
                throw new BlankException("Enter a valid message");
            }
            if (text.length() > 254) {
                throw new ExcessiveCharactersException("Message is too long");
            }
        }
        catch(DataAccessException e) {
            new CustomDataAccessException("Something went wrong", e);
        }

        return rowsAffected;
    }

    public List<Message> retrieveAllMessagesForUser(Integer accountId) {

        List<Integer> user = new ArrayList<>();
        user.add(accountId);
        try {
            return messageRepository.findAllById(user);
        }
        catch (DataAccessException e) {
            new CustomDataAccessException("Something went wrong", e);
        }

        return null;
    }

}