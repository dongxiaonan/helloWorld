package com.trailblazers.freewheelers.model;

import java.util.*;

public class ItemGridValidation {

    private final ItemValidation itemValidation = new ItemValidation();

    public ItemGrid getItemGridForValidItems(ItemGrid itemGrid) {
        List<Item> validItems = new LinkedList<Item>();
        for (Item item : itemGrid.getItems()) {
            if (itemValidation.validate(item).isEmpty()){
                validItems.add(item);
            }
        }
        return new ItemGrid(validItems);
    }


    public Map<Long, Map<String, String>> validateItemGrids(ItemGrid itemGrid) {
        Map<Long, Map<String, String>> errors = new HashMap<Long, Map<String, String>>();

        for (Item item : itemGrid.getItems()) {
            Map<String,String> itemErrors = itemValidation.validate(item);
            if (!itemErrors.isEmpty()) {
                errors.put(item.getItemId(), itemErrors);
            }
        }

        return errors;
    }
}


