package com.trailblazers.freewheelers.web;

import com.trailblazers.freewheelers.model.*;
import com.trailblazers.freewheelers.service.AccountService;
import com.trailblazers.freewheelers.service.ItemService;
import com.trailblazers.freewheelers.service.ReserveOrderService;
import com.trailblazers.freewheelers.service.impl.AccountServiceImpl;
import com.trailblazers.freewheelers.service.impl.ItemServiceImpl;
import com.trailblazers.freewheelers.service.impl.ReserveOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.valueOf;

@Controller
public class AdminController {

    static final String URL = "/admin";

    @Autowired
    ReserveOrderService reserveOrderService;
    @Autowired
    ItemService itemService;
    @Autowired
    AccountService accountService;

    @RequestMapping(value = URL, method = RequestMethod.GET)
    public void get(Model model) {
        model.addAttribute("reserveOrders", getAllOrders());
    }

    @RequestMapping(value = URL, method = RequestMethod.POST, params="save=Save Changes")
    public void updateOrder(Model model, String state, String orderId, String note) {
        OrderStatus status = OrderStatus.valueOf(state);
        reserveOrderService.updateOrderDetails(valueOf(orderId), status, note);
        get(model);
    }

    protected List<ReservedOrderDetail> getAllOrders() {
        List<ReserveOrder> reserveOrders = reserveOrderService.getAllOrdersByAccount();

        List<ReservedOrderDetail> reservedOrderDetails = new ArrayList<ReservedOrderDetail>();

        for (ReserveOrder reserveOrder : reserveOrders){
            Account account = accountService.get(reserveOrder.getAccount_id());

            List<Item> items = new ArrayList<Item>();

            for(OrderItem orderItem:reserveOrder.getOrderItems()) {
                Item item = itemService.getById(orderItem.getItemId());
                items.add(item);
            }
            reservedOrderDetails.add(new ReservedOrderDetail(reserveOrder.getOrder_id(),
                                                             account,
                                                             items,
                                                             reserveOrder.getReservation_timestamp(),
                                                             reserveOrder.getStatus(),
                                                             reserveOrder.getNote()));
        }
        return reservedOrderDetails;
    }

}
