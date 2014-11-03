package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.ReserveOrder;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Date;

@Controller
@Scope("session")
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ReserveOrderService reserveOrderService;

    final String sessionItem = "sessionItem";

    @RequestMapping(value = {"/myShoppingCart"}, method = RequestMethod.GET)
    public void showShoppingCart(Model model,@ModelAttribute Item item, HttpServletRequest request) {
        Item itemToCheckout =  itemService.getById(item.getItemId());

        if(itemToCheckout == null) {
            itemToCheckout = (Item) request.getSession().getAttribute(sessionItem);
        }

        model.addAttribute("item", itemToCheckout);
    }

    @RequestMapping(value = {"/myShoppingCart"}, method = RequestMethod.POST)
    public String addToShoppingCart(Model model, @ModelAttribute Item item, HttpServletRequest request){
        Item itemToCheckout =  itemService.getById(item.getItemId());

        if(itemToCheckout != null)
            request.getSession().setAttribute(sessionItem, itemToCheckout);

        model.addAttribute("item", itemToCheckout);
        return "redirect:/?q=t";
    }

    @RequestMapping(value = {"/confirmation/{orderId:.*}"}, method = RequestMethod.GET)
    public String confirmation(Model model, @PathVariable long orderId) {
        ReserveOrder reserveOrder = reserveOrderService.getOrderById(orderId);
        Item item = itemService.getById(reserveOrder.getItem_id());
        model.addAttribute("item", item);
        return "/shoppingCart/confirmation";
    }

    @RequestMapping(value = {"/checkout"}, method = RequestMethod.POST)
    public String checkoutItem(Model model, Principal principal, @ModelAttribute("item") Item item, HttpServletRequest request) {
        Item itemToReserve =  itemService.getById(item.getItemId());
        String userName = principal.getName();
        Account account =  accountService.getAccountByName(userName);
        Date rightNow = new Date();


        if(itemService.checkItemsQuantityIsMoreThanZero(item.getItemId())) {
            ReserveOrder reserveOrder = new ReserveOrder(account.getAccount_id(), itemToReserve.getItemId(), rightNow );
            reserveOrderService.save(reserveOrder);
            itemService.decreaseQuantityByOne(itemToReserve);
            model.addAttribute("item", itemToReserve);
            request.getSession().setAttribute(sessionItem, null);
            return "redirect:/shoppingCart/confirmation/" + reserveOrder.getOrder_id();
        } else {
            model.addAttribute("quantityErrorMessage", "Sorry, item is no longer available.");
            model.addAttribute("item", itemToReserve);
            request.getSession().setAttribute(sessionItem, null);
            return "/shoppingCart/myShoppingCart";
        }

    }

    @RequestMapping(value = {"/clear"}, method = RequestMethod.POST)
    public String clearShoppingCart(HttpServletRequest request) {
        request.getSession().setAttribute(sessionItem, null);

        return "redirect:/";
    }
}
