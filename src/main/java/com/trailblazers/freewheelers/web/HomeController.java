package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.impl.ItemServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class HomeController {

    private ItemService itemService ;

    public HomeController() {
        itemService = new ItemServiceImpl();
    }

	@RequestMapping(method = RequestMethod.GET)
	public String get(Model model, @ModelAttribute("item") Item item, HttpServletRequest request) {
        model.addAttribute("items", itemService.getItemsWithNonZeroQuantity());
        if(request.getParameter("q") != null && request.getParameter("q").equals("t")) {
            model.addAttribute("successMessage", "Item has been added to your shopping cart.");
        }
        return "home";
	}

}

