package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.OrderItem;
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
import com.trailblazers.freewheelers.FreeWheelersServer;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    final String sessionItems = "sessionItems";

    @RequestMapping(value = {"/myShoppingCart"}, method = RequestMethod.GET)
    public void showShoppingCart(Model model,@ModelAttribute Item item, HttpServletRequest request) {
        if(FreeWheelersServer.enabledFeatures.contains("multipleItemsPerCart")) {
            List<Item> cartItems = getSessionItems(request);

            model.addAttribute("items", cartItems);
        }
    }

    @RequestMapping(value = {"/myShoppingCart"}, method = RequestMethod.POST)
    public String addToShoppingCart(Model model, @ModelAttribute Item item, HttpServletRequest request){
       Item itemToCheckout =  itemService.getById(item.getItemId());
       ArrayList<Item> cartItems = getSessionItems(request);
       if(FreeWheelersServer.enabledFeatures.contains("multipleItemsPerCart")) {

           if (cartItems == null) {
               cartItems = new ArrayList<Item>();
           }

           if (itemToCheckout != null) {
               cartItems.add(itemToCheckout);
               setSessionAttributes(request, cartItems);
           }
           model.addAttribute("items", cartItems);
       }
       else if(itemToCheckout!=null) {
           cartItems = new ArrayList<Item>();
           cartItems.add(itemToCheckout);
           setSessionAttributes(request, cartItems);
           model.addAttribute("items",cartItems);
       }

        return "redirect:/?q=t";
    }

    private ArrayList<Item> getSessionItems(HttpServletRequest request) {
        return (ArrayList<Item>) request.getSession().getAttribute(sessionItems);
    }

    @RequestMapping(value = {"/confirmation/{orderId:.*}"}, method = RequestMethod.GET)
    public String confirmation(Model model, @PathVariable long orderId) {
        ReserveOrder reserveOrder = reserveOrderService.getOrderById(orderId);
        ArrayList<Item> orderItems = new ArrayList<Item>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(OrderItem orderItem:reserveOrder.getOrderItems()) {
            Item item = itemService.getById(orderItem.getItemId());
            item.setQuantity(orderItem.getQuantity());
            totalPrice = totalPrice.add(item.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
            orderItems.add(item);
        }
        model.addAttribute("items", orderItems);
        model.addAttribute("totalPrice",totalPrice);
        return "/shoppingCart/confirmation";
    }

    @RequestMapping(value = {"/checkout"}, method = RequestMethod.POST)
    public String checkoutItem(Model model, Principal principal, @ModelAttribute("items") ArrayList<Item> items, HttpServletRequest request) {
        if(items.size()==0 ) {
            items = getSessionItems(request);
        }
        String userName = principal.getName();
        Date rightNow = new Date();
        Account account = accountService.getAccountByName(userName);
        ReserveOrder reserveOrder = new ReserveOrder(account.getAccount_id(), new ArrayList<OrderItem>(), rightNow);
        List<Item> itemsNotAvailable = new ArrayList<Item>();

        boolean allItemsAreAvailable = true;
        for(Item item:items) {
            Item itemToReserve = itemService.getById(item.getItemId());

            if (itemService.checkItemsQuantityIsMoreThanZero(item.getItemId())) {
                reserveOrder.addItemToOrder(itemToReserve.getItemId(),1L);
                itemService.decreaseQuantityByOne(itemToReserve);
            } else {
                allItemsAreAvailable = false;
                model.addAttribute("quantityErrorMessage", "Sorry, some items are no longer available and have been removed from your cart.");
                itemsNotAvailable.add(item);
            }
        }
        items.removeAll(itemsNotAvailable);
        model.addAttribute("items", items);
        if(allItemsAreAvailable) {
            reserveOrderService.save(reserveOrder);
            clearSessionAttributes(request);
            return "redirect:/shoppingCart/confirmation/" + reserveOrder.getOrder_id();
        }
        setSessionAttributes(request, items);
        return "/shoppingCart/myShoppingCart";
    }

    private void clearSessionAttributes(HttpServletRequest request) {
        setSessionAttributes(request, null);
    }

    private void setSessionAttributes(HttpServletRequest request, ArrayList<Item> items) {
        BigDecimal cartPrice = BigDecimal.ZERO;
        if(items != null){
            for(Item item : items) {
                cartPrice = cartPrice.add(item.getPrice());
            }
        }
        request.getSession().setAttribute(sessionItems, items);
        String totalCartPrice = "totalCartPrice";
        request.getSession().setAttribute(totalCartPrice, cartPrice);
    }

    @RequestMapping(value = {"/clear"}, method = RequestMethod.POST)
    public String clearShoppingCart(HttpServletRequest request) {
        clearSessionAttributes(request);
        return "redirect:/";
    }
}
