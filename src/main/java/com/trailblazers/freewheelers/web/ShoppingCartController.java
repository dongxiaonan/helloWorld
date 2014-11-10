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
import org.springframework.transaction.annotation.Transactional;
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

    @RequestMapping(value = {"/addToCart"}, method = RequestMethod.POST)
    public String addToShoppingCart(Model model, @ModelAttribute Item item, HttpServletRequest request){
       Item itemToCheckout =  itemService.getById(item.getItemId());
       ArrayList<Item> cartItems = getSessionItems(request);
       if(FreeWheelersServer.enabledFeatures.contains("multipleItemsPerCart")) {

           if (cartItems == null) {
               cartItems = new ArrayList<Item>();
           }

           if (itemToCheckout != null) {
               if(!cartItems.contains(itemToCheckout)) {
                   if(itemToCheckout.getQuantity() > 0) {
                       itemToCheckout.setQuantity(1l);
                       cartItems.add(itemToCheckout);
                   } else {
                       request.getSession().setAttribute("error",true);
                   }
               } else {
                   Item itemToBeModified = cartItems.get(cartItems.indexOf(itemToCheckout));
                   if(itemToCheckout.getQuantity() > itemToBeModified.getQuantity()) {
                       itemToBeModified.setQuantity(itemToBeModified.getQuantity() + 1);
                   } else {
                       request.getSession().setAttribute("error",true);
                   }
               }
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

        request.getSession().setAttribute("lastItem",itemToCheckout);
        return "redirect:/";
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

    @Transactional
    @RequestMapping(value = {"/checkout"}, method = RequestMethod.POST)
    public String checkoutItem(Model model, Principal principal, @ModelAttribute("items") ArrayList<Item> items, HttpServletRequest request) {
        if(items.size()==0 ) {
            items = getSessionItems(request);
        }

        List<Item> unavailableItems = getUnavailableItems(items);
        if(unavailableItems.size()==0) {
            String userName = principal.getName();
            Account account = accountService.getAccountByName(userName);
            Date rightNow = new Date();
            ReserveOrder reserveOrder = getOrderFor(account.getAccount_id(),items,rightNow);
            reserveOrderService.save(reserveOrder);
            updateItemsQuantity(items);
            clearSessionAttributes(request);
            model.addAttribute("items", items);
            return "redirect:/shoppingCart/confirmation/" + reserveOrder.getOrder_id();
        }
        else {
            removeUnavailableItems(items, unavailableItems);
            model.addAttribute("items", items);
            model.addAttribute("quantityErrorMessage", "Sorry, some items are no longer available and have been removed from your cart.");
        }
        setSessionAttributes(request, items);
        return "/shoppingCart/myShoppingCart";
    }

    private ReserveOrder getOrderFor(Long account_id, ArrayList<Item> items, Date rightNow) {
        ReserveOrder reserveOrder = new ReserveOrder(account_id, new ArrayList<OrderItem>(), rightNow);
        for(Item item:items) {
            reserveOrder.addItemToOrder(item.getItemId(),item.getQuantity());
        }
        return reserveOrder;
    }

    private void updateItemsQuantity(ArrayList<Item> items) {
        for(Item item:items) {
            Item itemInDB = itemService.getById(item.getItemId());
            itemService.decreaseQuantity(itemInDB, item.getQuantity());
        }
    }

    private List<Item> getUnavailableItems(ArrayList<Item> items) {
        List<Item> itemsNotAvailable = new ArrayList<Item>();
        for(Item item:items) {
            Item itemFromDB  =itemService.getById(item.getItemId());
            if(itemFromDB.getQuantity()<item.getQuantity()) {
                itemsNotAvailable.add(item.setQuantity(itemFromDB.getQuantity()));
            }
        }

        return itemsNotAvailable;
    }

    private List<Item> removeUnavailableItems(ArrayList<Item> items, List<Item> itemsNotAvailable) {
        for(Item unavailableItem : itemsNotAvailable) {
            if(unavailableItem.getQuantity()==0) {
                items.remove(unavailableItem);
            }
        }
        return items;
    }

    private void clearSessionAttributes(HttpServletRequest request) {
        setSessionAttributes(request, null);
    }

    private void setSessionAttributes(HttpServletRequest request, ArrayList<Item> items) {
        BigDecimal cartPrice = BigDecimal.ZERO;
        if(items != null){
            for(Item item : items) {
                cartPrice = cartPrice.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
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
