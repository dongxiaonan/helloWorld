package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.ItemType;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ServiceResult;
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

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebAppConfiguration
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/*",})

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
}
