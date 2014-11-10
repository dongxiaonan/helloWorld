package com.trailblazers.freewheelers.service.impl;

import com.trailblazers.freewheelers.mappers.ItemMapper;
import com.trailblazers.freewheelers.mappers.MyBatisUtil;
import com.trailblazers.freewheelers.mappers.OrderItemsMapper;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.ItemValidation;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ServiceResult;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {

    private final SqlSession sqlSession;
    private final ItemMapper itemMapper;

    public ItemServiceImpl() {
        this(MyBatisUtil.getSqlSessionFactory().openSession());
    }

    protected ItemServiceImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.itemMapper = sqlSession.getMapper(ItemMapper.class);
    }

    @Override
    public Item getById(Long item_id) {
        sqlSession.clearCache();
        return itemMapper.getById(item_id);
    }

    @Override
    public Item getByName(String name) {
        return itemMapper.getByName(name);
    }

    @Override
    public void delete(Item item) {
        OrderItemsMapper ordertIemMapper = sqlSession.getMapper(OrderItemsMapper.class);
        ordertIemMapper.deleteByItemId(item.getItemId());
        itemMapper.delete(item);
        sqlSession.commit();
    }

    @Override
    public List<Item> findAll() {
        sqlSession.clearCache();
        return itemMapper.findAll();
    }

    @Override
    public List<Item> getItemsWithNonZeroQuantity() {
        sqlSession.clearCache();
        List<Item> listFromDB = itemMapper.findAvailable();
        Collections.sort(listFromDB);
        return listFromDB;
    }

    @Override
    public void saveAll(List<Item> items) {
        for (Item item : items) {
            insertOrUpdate(item);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteItems(List<Item> items) {
        for (Item item : items) {
            delete(item);
        }
    }

    @Override
    public void decreaseQuantity(Item item, Long quantity) {
        item.setQuantity(item.getQuantity() - quantity);
        itemMapper.update(item);
        sqlSession.commit();
    }

    @Override
    public ServiceResult<Item> saveItem(Item item) {
        Map<String,String> errors = new ItemValidation().validate(item);

        if (errors.isEmpty()) {
            insertOrUpdate(item);
            sqlSession.commit();
        }
        return new ServiceResult<Item>(errors, item);
    }

    @Override
    public boolean checkItemsQuantityIsMoreThanZero(long itemId) {
        sqlSession.clearCache();
        return itemMapper.getById(itemId).getQuantity()>0;
    }

    private void insertOrUpdate(Item item) {
        if (item.getItemId() == null) {
            itemMapper.insert(item);
        } else {
            itemMapper.update(item);
        }
    }
}
