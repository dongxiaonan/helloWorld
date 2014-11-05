package com.trailblazers.freewheelers.web;


import com.trailblazers.freewheelers.model.Account;
import com.trailblazers.freewheelers.model.Item;
import com.trailblazers.freewheelers.model.OrderItem;
import com.trailblazers.freewheelers.model.ReserveOrder;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import com.trailblazers.freewheelers.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/userProfile")
public class UserProfileController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ReserveOrderService reserveOrderService;
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/{userName:.*}", method = RequestMethod.GET)
    public String get(@PathVariable String userName, Model model, Principal principal) {

        if (userName == null) {
            userName = principal.getName();
        }

        userName = decode(userName);

        if (!userName.equals(principal.getName()) && !isAdmin(principal)){
            return "accessDenied";
        }

        Account account = accountService.getAccountByName(userName);

        List<Item> items = getItemsOrderByUser(account);

        model.addAttribute("items", items);
        model.addAttribute("userDetail", account);
        model.addAttribute("address", account.getAddress().replaceAll("\\n", "<br/>"));

        return "userProfile";
    }

    private boolean isAdmin(Principal principal) {
        return accountService.getAccountRoleByName(principal.getName()).getRole().equals(AccountServiceImpl.ADMIN);
    }

    private String decode(String userName)  {
        try {
            return URLDecoder.decode(userName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return userName;
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String get(Model model, Principal principal) {
        return get(null, model, principal);
    }

    private List<Item> getItemsOrderByUser(Account account) {
        List<ReserveOrder> reserveOrders = reserveOrderService.findAllOrdersByAccountId(account.getAccount_id());
        List<Item> items = new ArrayList<Item>();
        for (ReserveOrder reserveOrder : reserveOrders) {
            for(OrderItem orderItem:reserveOrder.getOrderItems()) {
                items.add(itemService.getById(orderItem.getItemId()));
            }
        }
        return items;
    }


}
