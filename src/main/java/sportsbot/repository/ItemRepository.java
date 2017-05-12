package sportsbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sportsbot.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}
