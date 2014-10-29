package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Item;
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

@Controller
@RequestMapping(ItemController.ITEM_PAGE)
public class ItemController{

	static final String ITEM_PAGE = "/item";
	static final String ITEM_LIST_PAGE = "/itemList";

    @Autowired
    ItemService itemService;


	@RequestMapping(method = RequestMethod.GET)
	public String get(Model model, @ModelAttribute Item item) {
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
            ItemGrid itemGrid = new ItemGrid(itemService.findAll());
			model.addAttribute("itemGrid", itemGrid);
            model.addAttribute("itemTypes", ItemType.values());
			return ITEM_LIST_PAGE;
		}
		return "redirect:" + ITEM_PAGE;
	}


    @RequestMapping(method = RequestMethod.POST, params="update=Update all enabled items")
	public String updateItem(@ModelAttribute ItemGrid itemGrid) {
		itemService.saveAll(itemGrid.getItems());
		return "redirect:" + ITEM_PAGE;
	}

    @RequestMapping(method = RequestMethod.POST, params="delete=Delete all enabled items")
    public String deleteItem( @ModelAttribute ItemGrid itemGrid) {
        itemService.deleteItems(itemGrid.getItems());
        return "redirect:" + ITEM_PAGE;
    }
	
}
