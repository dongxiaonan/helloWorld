package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.ItemGrid;
import com.trailblazers.freewheelers.model.ItemType;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ServiceResult;
import com.trailblazers.freewheelers.service.impl.ItemServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/app-config.xml",
                                 "file:src/main/webapp/WEB-INF/spring/mvc-config.xml",
                                 "file:src/main/webapp/WEB-INF/spring/persistence-config.xml",
                                 "file:src/main/webapp/WEB-INF/spring/security-app-context.xml"})

@RunWith(SpringJUnit4ClassRunner.class)
public class ItemControllerTest {

    @Mock
    ItemService itemService;
    @Mock
    SqlSession sqlSession;

    @Autowired
    protected WebApplicationContext webAppContext;
    protected MockMvc mockMVC;

    Model responseModel;
    ItemGrid itemGrid;
    Item item;
    ItemController itemController;

    @Before
    public void setUp(){
        initMocks(this);
        itemController = new ItemController();
        itemController.itemService = itemService;
        responseModel = new ExtendedModelMap();
        item = new Item();
        itemGrid = new ItemGrid(asList(item));
        mockMVC = webAppContextSetup(webAppContext).build();
    }

    @Test
    public void shouldRenderItemListView() throws Exception {
        String returnedPath = itemController.get(responseModel, item);
        assertThat(returnedPath, is(ItemController.ITEM_LIST_PAGE));
    }

    @Test
    public void shouldReturnItemsForDisplay() throws Exception {

        itemGrid = new ItemGrid();
        when(itemService.findAll()).thenReturn(itemGrid.getItems());

        itemController.get(responseModel, item);

        verify(itemService).findAll();
        ItemGrid returnedItemGrid = (ItemGrid) responseModel.asMap().get("itemGrid");
        assertThat(returnedItemGrid.getItems(), is(itemGrid.getItems()));
        assertThat((ItemType[]) responseModel.asMap().get("itemTypes"), is(ItemType.values()));

    }

    @Test
    public void shouldDisplayItemsAfterSavingGivenItem(){

        Map errors = new HashMap<String, String>();
        ServiceResult<Item> serviceResultNoErrors = new ServiceResult<Item>(errors, item);
        when(itemService.saveItem(item)).thenReturn(serviceResultNoErrors);

        String returnedPath = itemController.post(responseModel, item, mock(BindingResult.class));

        verify(itemService).saveItem(item);
        assertThat(returnedPath, is("redirect:" + ItemController.ITEM_PAGE));

    }

    @Test
    public void shouldDisplayErrorsIfAnyAfterSavingItem(){

        Map errors = new HashMap<String, String>();
        errors.put("name", "your name is empty");
        ServiceResult<Item> serviceResultWithErrors = new ServiceResult<Item>(errors, item);

        when(itemService.saveItem(item)).thenReturn(serviceResultWithErrors);
        when(itemService.findAll()).thenReturn(asList(item));

        String returnedPath = itemController.post(responseModel, item, mock(BindingResult.class));

        assertThat((HashMap<String, String>) responseModel.asMap().get("errors"), is(errors));
        verify(itemService).findAll();
        ItemGrid returnedItemGrid = (ItemGrid) responseModel.asMap().get("itemGrid");
        assertThat(returnedItemGrid.getItems(), is(itemGrid.getItems()));
        assertThat((ItemType[]) responseModel.asMap().get("itemTypes"), is(ItemType.values()));
        assertThat(returnedPath, is(ItemController.ITEM_LIST_PAGE));
    }

    @Test
    public void shouldReturnModelWithErrorKeyInItWhenPostingRequestWithACharForQuantity() throws Exception{
        RequestBuilder requestBuilder = post(ItemController.ITEM_PAGE)
                                            .param("name", "item")
                                            .param("price", "1")
                                            .param("type", "ACCESSORIES")
                                            .param("description", "some descripton")
                                            .param("quantity", "a");

        MvcResult result = mockMVC.perform(requestBuilder).andReturn();
        Map<String, String> quantityError = (Map<String, String>) result.getModelAndView().getModel().get("errors");

        assertThat(quantityError.containsKey("quantity"), is(true));
    }

    @Test
    public void shouldReturnModelWithTheKeysOfInvalidItemsWhenUpdatingOneItemWithInvalidQuantity() throws Exception {
        RequestBuilder requestBuilder = post(ItemController.ITEM_PAGE)
                                            .param("update", "Update all enabled items")
                                            .param("itemMap[13].name", "Item name")
                                            .param("itemMap[13].itemId", "13")
                                            .param("itemMap[13].price", "19")
                                            .param("itemMap[13].description", "Item Desc")
                                            .param("itemMap[13].type", "ACCESSORIES")
                                            .param("itemMap[13].quantity", "a");

        MvcResult result = mockMVC.perform(requestBuilder).andReturn();

        Map<Long, Map<String, String>> errors = (Map<Long, Map<String, String>>) result.getModelAndView().getModel().get("itemGridErrors");

        assertTrue(errors.size() == 1);
    }

    @Test
    public void shouldReturnErrorIfQuantityIsZero() throws Exception {
        RequestBuilder requestBuilder = post(ItemController.ITEM_PAGE)
                .param("update", "Update all enabled items")
                .param("itemMap[13].name", "Item name")
                .param("itemMap[13].itemId", "13")
                .param("itemMap[13].price", "19")
                .param("itemMap[13].description", "Item Desc")
                .param("itemMap[13].type", "ACCESSORIES")
                .param("itemMap[13].quantity", "0");

        MvcResult result = mockMVC.perform(requestBuilder).andReturn();

        Map<Long, Map<String, String>> errors = (Map<Long, Map<String, String>>) result.getModelAndView().getModel().get("itemGridErrors");

        assertTrue(errors.size() == 1);
    }

    @Test
    public void shouldReturnModelWithErrorKeyWhenUpdatingTwoItemsWhereOneItemHasInvalidQuantity() throws Exception{
        RequestBuilder requestBuilder = post(ItemController.ITEM_PAGE)
                                            .param("update", "Update all enabled items")
                                            .param("itemMap[1].name", "Item name")
                                            .param("itemMap[1].itemId", "13")
                                            .param("itemMap[1].price", "19")
                                            .param("itemMap[1].description", "Item Desc")
                                            .param("itemMap[1].type", "ACCESSORIES")
                                            .param("itemMap[1].quantity", "7")
                                            .param("itemMap[10].name", "Item name")
                                            .param("itemMap[10].itemId", "13")
                                            .param("itemMap[10].price", "19")
                                            .param("itemMap[10].description", "Item Desc")
                                            .param("itemMap[10].type", "ACCESSORIES")
                                            .param("itemMap[10].quantity", "a");
        MvcResult result = mockMVC.perform(requestBuilder).andReturn();

        Map<Long, Map<String, String>> errors = (Map<Long, Map<String, String>>) result.getModelAndView().getModel().get("itemGridErrors");

        assertTrue(errors.size() == 1);
    }

    @Test
    public void shouldInvokeSaveAllWithOnlyValidItems() throws Exception {
        ItemController con = new ItemController();
        ItemService serviceMock = mock(ItemServiceImpl.class);
        con.itemService = serviceMock;

        ItemGrid invalidItemGrid = new ItemGrid(Arrays.asList(invalidItem()));
        con.updateItem(mock(Model.class), invalidItemGrid, mock(BindingResult.class));

        verify(serviceMock, times(1)).saveAll(new ArrayList<Item>());
    }

    @Test
    public void shouldNotRedirectInCaseOfAnError() throws Exception {
        ItemController con = new ItemController();
        ItemService serviceMock = mock(ItemServiceImpl.class);
        con.itemService = serviceMock;

        ItemGrid invalidItemGrid = new ItemGrid(Arrays.asList(invalidItem()));
        String destination = con.updateItem(mock(Model.class), invalidItemGrid, mock(BindingResult.class));
        assertEquals(ItemController.ITEM_LIST_PAGE, destination);
    }

    @Test
    public void shouldAddAttributeForItemToModel() throws Exception {
        ItemController con = new ItemController();
        ItemService serviceMock = mock(ItemServiceImpl.class);
        Model model = mock(Model.class);
        con.itemService = serviceMock;

        ItemGrid invalidItemGrid = new ItemGrid(Arrays.asList(invalidItem()));
        con.updateItem(model, invalidItemGrid, mock(BindingResult.class));

        //TODO: FS/KP fix the problem with matching any object and any collection

        verify(model, atLeast(1)).addAttribute(eq("itemGridErrors"), anyCollection());
        verify(model, atLeast(1)).addAttribute(eq("item"), anyObject());
        verify(model, atLeast(1)).addAttribute(eq("itemGrid"), anyObject());
        verify(model, atLeast(1)).addAttribute(eq("itemTypes"), anyObject());
    }

    private Item invalidItem() {
        return new Item().setName("asdf").setItemId(1L)
                .setPrice(new BigDecimal(123)).setDescription("asdf")
                .setType(ItemType.ACCESSORIES).setQuantity(0L);
    }


}

