package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.ItemGrid;
import com.trailblazers.freewheelers.model.ItemGridValidation;
import com.trailblazers.freewheelers.model.ItemType;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@RequestMapping(ItemController.ITEM_PAGE)
public class ItemController{

	static final String ITEM_PAGE = "/item";
	static final String ITEM_LIST_PAGE = "/itemList";

    @Autowired
    ItemService itemService;


	@RequestMapping(method = RequestMethod.GET)
	public String get(Model model, @ModelAttribute Item item) {
        return itemList(model);
    }

    private String itemList(Model model) {
        ItemGrid itemGrid = new ItemGrid(itemService.findAll());
        model.addAttribute("itemGrid", itemGrid);
        model.addAttribute("itemTypes", ItemType.values());
        return ITEM_LIST_PAGE;
    }

    @RequestMapping(method = RequestMethod.POST)
	public String post(Model model, @ModelAttribute("item") Item item, BindingResult bindingResult) {

        if (bindingResult.hasErrors() && bindingResult.getFieldError("quantity") != null){
            item.setQuantity(0l);
        }
        ServiceResult<Item> result = itemService.saveItem(item);

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getErrors());
            return itemList(model);
        }
		return "redirect:" + ITEM_PAGE;
	}


    @RequestMapping(method = RequestMethod.POST, params="update=Update all enabled items")
	public String updateItem(Model model, @ModelAttribute ItemGrid itemGrid, BindingResult bindingResult) {
        ItemGridValidation validation = new ItemGridValidation();
        Map<Long, Map<String, String>> errors = validation.validateItemGrids(itemGrid);
        model.addAttribute("itemGridErrors", errors);
        if (bindingResult.getFieldErrorCount()>0 || !errors.isEmpty()){
            itemService.saveAll(validation.getItemGridForValidItems(itemGrid).getItems());
            model.addAttribute("item", new Item());
            return itemList(model);
        }

        itemService.saveAll(itemGrid.getItems());
        return "redirect:" + ITEM_PAGE;
    }

    @RequestMapping(method = RequestMethod.POST, params="delete=Delete all enabled items")
    public String deleteItem( @ModelAttribute ItemGrid itemGrid) {
        itemService.deleteItems(itemGrid.getItems());
        return "redirect:" + ITEM_PAGE;
    }

}
