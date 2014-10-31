package com.trailblazers.freewheelers.service;

import com.trailblazers.freewheelers.model.Item;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ItemService {
	
	Item getById(Long item_id);

    Item getByName(String name);

	void delete(Item item);
	
	List<Item> findAll();

    List<Item> getItemsWithNonZeroQuantity();
	
	void saveAll(List<Item> items);

    void deleteItems(List<Item> items);

    void decreaseQuantityByOne(Item item);

    ServiceResult<Item> saveItem(Item item);

    long checkItemsQuantityIsMoreThanZero(long itemId);
}
