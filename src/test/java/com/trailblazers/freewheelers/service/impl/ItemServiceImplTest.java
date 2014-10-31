package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.ItemMapper;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.service.ItemService;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ItemServiceImplTest {

    @Mock
    ItemMapper itemMapper;

    @Mock
    SqlSession sqlSession;

    ItemService itemService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(sqlSession.getMapper(ItemMapper.class)).thenReturn(itemMapper);
        itemService = new ItemServiceImpl(sqlSession);
    }

    @Test
    public void shouldGetItemByIdFromMapper(){
        Long itemId = 1L;
        Item expectedItem = new Item();
        when((itemMapper.getById(itemId))).thenReturn(expectedItem);

        Item returnedItem = itemService.getById(itemId);
        verify(itemMapper).getById(itemId);
        assertThat(returnedItem, is(expectedItem));
    }

    @Test
    public void shouldGetItemByNameFromMapper(){
        String name = "name";
        Item expectedItem = new Item();
        when((itemMapper.getByName(name))).thenReturn(expectedItem);

        Item returnedItem = itemService.getByName(name);
        verify(itemMapper).getByName(name);
        assertThat(returnedItem, is(expectedItem));
    }

    @Test
    public void shouldFindAllItemsFromMapper(){
        List<Item> expectedItems = new ArrayList<Item>();
        when((itemMapper.findAll())).thenReturn(expectedItems);

        List<Item> returnedItems = itemService.findAll();
        verify(itemMapper).findAll();
        assertThat(returnedItems, is(expectedItems));
    }

    @Test
    public void shouldFindAllAvailableItemsFromMapper(){
        List<Item> expectedItems = new ArrayList<Item>();
        when((itemMapper.findAvailable())).thenReturn(expectedItems);

        List<Item> returnedItems = itemService.getItemsWithNonZeroQuantity();
        verify(itemMapper).findAvailable();
        assertThat(returnedItems, is(expectedItems));
    }

    @Test
    public void shouldReturnTheItemQuantity() {
        Long itemId = 1L;
        long expectedQuantity = 2;
        when(itemMapper.getById(itemId)).thenReturn(new Item().setQuantity(expectedQuantity));

        long returnedQuantity = itemService.checkItemsQuantityIsMoreThanZero(itemId);

        verify(itemMapper, times(1)).getById(itemId);
        assertThat(returnedQuantity, is(expectedQuantity));
    }

}
