package com.sn.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sn.dbo.CustomerSOS;
import com.sn.dbo.OrderLineItem;
import com.sn.dbo.SalesOrder;
import com.sn.repository.OrderLineRpo;
import com.sn.repository.SalesOrderRepo;
import com.sn.service.ICustomerService;
import com.sn.service.ItemService;
import com.sn.vo.ItemVO;
import com.sn.vo.OrderDetailsVO;

@RestController
@RequestMapping("/service3")
public class CustSOSController {

	private static final Logger logger = LoggerFactory.getLogger(CustSOSController.class);

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private SalesOrderRepo salesOrderRepo;

	@Autowired
	private OrderLineRpo orderLineRpo;

	@Autowired
	private ItemService itemService;

	@RequestMapping(value = "/getAllCust", method = RequestMethod.GET)
	private List<CustomerSOS> getAllCustSOS() {
		return customerService.getAllCustSOS();
	}

	@RequestMapping(value = "/salesorder", method = RequestMethod.GET)
	private List<SalesOrder> getAllSalesOrder() {
		return salesOrderRepo.findAll();
	}

	@RequestMapping(value = "/orderline", method = RequestMethod.GET)
	private List<OrderLineItem> getAllOrderLine() {
		return orderLineRpo.findAll();
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	private OrderDetailsVO test() {
		OrderDetailsVO order = new OrderDetailsVO();
		List<String> items = new ArrayList<String>();
		items.add("gold");
		items.add("silver");

		order.setItems(items);
		return order;
	}

	@RequestMapping(value = "/orders", method = RequestMethod.POST)
	private String addOrder(@RequestBody OrderDetailsVO order) {
		logger.info("addOrder ############# for cust id {} ", order.getCustId());
		CustomerSOS custSOS = customerService.getCustSOS(order.getCustId());
		logger.info("addOrder ######## customer {} ",custSOS.getFirstName());
		SalesOrder salesOrder = null;

		try {
			if (custSOS.getCustId() > 0) {
				salesOrder = new SalesOrder(order.getOrderDate(), order.getCustId(), order.getOrderDesc(), 0l);
				for (String item : order.getItems()) {
					logger.info("addOrder ----------22222 ");
					ItemVO itemVO = itemService.getItems(item);
					logger.info("addOrder ----------for item desc {} ", itemVO.getDescription());
					salesOrder.setTotalPrice(salesOrder.getTotalPrice() + itemVO.getPrice());
					salesOrder = salesOrderRepo.save(salesOrder);
					orderLineRpo.save(new OrderLineItem(itemVO.getName(), "1", salesOrder.getId()));
				}

				return "Order created";

			}
		} catch (Exception e) {
			logger.error("addOrder error {}",e.getMessage());
			return "Order Exception";
		}
		return "Order Not created";

	}

}
