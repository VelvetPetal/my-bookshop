package com.example.MyBookShopApp.repositories;

import com.example.MyBookShopApp.model.book.review.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

}
