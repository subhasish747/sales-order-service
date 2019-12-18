package com.sn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sn.vo.ItemVO;

@Service
public class ItemService implements IItemService {
	
	private 

	@Autowired
	RestTemplate restTemplate;

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public ItemVO getItems(String itemname) {
		ItemVO item;
		try {
			item = this.restTemplate
					.getForObject("https://249071-item-service/service2/items/" + itemname, ItemVO.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return item;
	}

}
