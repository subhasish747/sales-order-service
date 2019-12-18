package com.sn.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sn.vo.ItemVO;

@Configuration
@RibbonClient(name = "249071-item-service")
public class ItemService_1  {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
 

	@Autowired
	RestTemplate restTemplate;

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public ItemVO getItems(String itemname) {
		ItemVO item;
		logger.info(" getItems  start ----------------");
		try {
			item = this.restTemplate
					.getForObject("https://249071-item-service/service2/items/" + itemname, ItemVO.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			logger.error(" error in getItems ",e.getMessage());
			e.printStackTrace();
			return null;
		}
		return item;
	}
	

}
