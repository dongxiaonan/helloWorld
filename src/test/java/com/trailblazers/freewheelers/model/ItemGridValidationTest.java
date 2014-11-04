package com.trailblazers.freewheelers.model;

import org.junit.Test;

import java.util.*;

import static java.math.BigDecimal.valueOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ItemGridValidationTest {

    @Test
    public void shouldReturnTheFirstAndLastItemsInTheGridItemWhenValidatingIt() throws Exception {
        ItemGridValidation itemGridValidation = new ItemGridValidation();
        List<Item> testItems = getTestItemsWithInvalidItem();
        ItemGrid itemGrid = new ItemGrid(testItems);

        ItemGrid expectedResults = new ItemGrid(Arrays.asList(testItems.get(0), testItems.get(2)));
        ItemGrid validItems = itemGridValidation.getItemGridForValidItems(itemGrid);

        assertThat(validItems.getItemMap().keySet(), is(expectedResults.getItemMap().keySet()));
    }

    @Test
    public void shouldReturnOneErrorForInvalidItemInTheItemGrid() throws Exception {
        ItemGridValidation itemGridValidation = new ItemGridValidation();
        ItemGrid itemGrid = new ItemGrid(getTestItemsWithInvalidItem());

        Map<Long,Map<String,String>> error = itemGridValidation.validateItemGrids(itemGrid);

        assertTrue(error.containsKey(3L));
        assertTrue(error.get(3L).containsKey("quantity"));
    }

    @Test
    public void shouldReturnErrorsForInvalidItemsInTheItemGrid() throws Exception {
        ItemGridValidation itemGridValidation = new ItemGridValidation();
        ItemGrid itemGrid = new ItemGrid(getTestItemsWithThreeInvalidItems());

        Map<Long,Map<String,String>> error = itemGridValidation.validateItemGrids(itemGrid);

        assertTrue(error.size() == 2);
        assertTrue(error.containsKey(2L));
        assertTrue(error.containsKey(8L));
    }

    @Test
    public void shouldMergeEmptyItemGrid() throws Exception {
        ItemGrid itemGrid = new ItemGrid();
        itemGrid.merge(new ItemGrid());
        assertThat(itemGrid.getItems().size(), is(0));
    }

    @Test
    public void shouldMergeOldAndNewItems() throws Exception {
        ItemGrid itemGrid = new ItemGrid();
        itemGrid.merge(new ItemGrid(Arrays.asList(someItem())));
        assertThat(itemGrid.getItems().size(), is(1));
    }

    @Test
    public void shouldReplaceOldItemWithNewItemWhenMerging() throws Exception {
        ItemGrid itemGrid = new ItemGrid(Arrays.asList(someItem().setItemId(1L).setName("I am old!")));
        itemGrid.merge(new ItemGrid(Arrays.asList(someItem().setItemId(1L).setName("I am new!"))));
        assertThat(itemGrid.getItems().size(), is(1));
        assertThat(itemGrid.getItems().get(0).getName(), is("I am new!"));
    }

    private Item someItem() {
        return new Item()
                .setName("Some Item")
                .setDescription("... with a description")
                .setType(ItemType.ACCESSORIES)
                .setPrice(valueOf(0.49))
                .setQuantity(99L);
    }

    private List<Item> getValidItemGrid(){
        List <Item> items = new ArrayList<Item>();
        items.add(someItem().setItemId(1L).setQuantity(10L));
        items.add(someItem().setItemId(3L).setQuantity(123L));
        items.add(someItem().setItemId(12L).setQuantity(9L));

        return items;
    }

    private List<Item> getTestItemsWithInvalidItem(){
        List <Item> items = new ArrayList<Item>();
        items.add(someItem().setItemId(1L).setQuantity(10L));
        items.add(someItem().setItemId(3L).setQuantity(null));
        items.add(someItem().setItemId(12L).setQuantity(9L));

        return items;
    }

    private List<Item> getTestItemsWithThreeInvalidItems(){
        List <Item> items = new ArrayList<Item>();
        items.add(someItem().setItemId(1L).setQuantity(10L));
        items.add(someItem().setItemId(2L).setQuantity(null));
        items.add(someItem().setItemId(12L).setQuantity(9L));
        items.add(someItem().setItemId(8L).setQuantity(null));

        return items;
    }



}