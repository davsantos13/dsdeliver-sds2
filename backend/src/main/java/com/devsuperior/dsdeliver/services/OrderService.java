package com.devsuperior.dsdeliver.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsdeliver.dto.OrderDTO;
import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.Order;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.entities.enums.OrderStatus;
import com.devsuperior.dsdeliver.repositories.OrderRepository;
import com.devsuperior.dsdeliver.repositories.ProductRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Transactional(readOnly = true)
	public List<OrderDTO> findAll() {
		List<Order> orders = orderRepository.findOrdersWithProducts();

		return orders.stream().map(o -> new OrderDTO(o)).collect(Collectors.toList());
	}

	@Transactional
	public OrderDTO save(OrderDTO orderDTO) {
		Order order = new Order(null, orderDTO.getAddress(), orderDTO.getLatitude(), orderDTO.getLongitude(), Instant.now(), orderDTO.getStatus(), orderDTO.getTotal());

		for (ProductDTO p : orderDTO.getProducts()) {
			Product product = productRepository.getOne(p.getId());
			order.getProducts().add(product);
		}

		order = orderRepository.save(order);

		return new OrderDTO(order);
	}

	@Transactional
	public OrderDTO updateStatus(Long id) {
		Order order = orderRepository.getOne(id);
		order.setStatus(OrderStatus.DELIVERED);
		order = orderRepository.save(order);

		return new OrderDTO(order);
	}
}
