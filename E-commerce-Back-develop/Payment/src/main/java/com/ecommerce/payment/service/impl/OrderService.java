package com.ecommerce.payment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.payment.client.InventoryClient;
import com.ecommerce.payment.client.ProductClient;
import com.ecommerce.payment.dto.request.OrderCreationDto;
import com.ecommerce.payment.dto.request.OrderDetailCreationDto;
import com.ecommerce.payment.dto.request.StockCreationDto;
import com.ecommerce.payment.dto.response.ModelDto;
import com.ecommerce.payment.dto.response.OrderDto;
import com.ecommerce.payment.dto.response.ProductDto;
import com.ecommerce.payment.dto.response.StockDto;
import com.ecommerce.payment.entity.Order;
import com.ecommerce.payment.entity.OrderDetail;
import com.ecommerce.payment.entity.OrderState;
import com.ecommerce.payment.mapper.impl.OrderDetailMapper;
import com.ecommerce.payment.mapper.impl.OrderMapper;
import com.ecommerce.payment.query.dto.OrderFilterDto;
import com.ecommerce.payment.query.dto.SortDto;
import com.ecommerce.payment.query.specification.OrderSpecification;
import com.ecommerce.payment.repository.OrderDetailRepository;
import com.ecommerce.payment.repository.OrderRepository;
import com.ecommerce.payment.service.IOrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
	
	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	
	private final InventoryClient inventoryClient;
	private final ProductClient productClient;
	
	private final OrderMapper orderMapper;
	private final OrderDetailMapper orderDetailMapper;

	@Override
    @Transactional
	public ResponseEntity<OrderDto> saveOrder(OrderCreationDto orderCreationDto) {	
		// If cart was empty, return error
		if (orderCreationDto.orderDetails().isEmpty()) {
			throw new IllegalArgumentException("No se ha podido procesar el pedido al no haber productos en el carrito.");
		}
	
		List<OrderDetailCreationDto> orderDetailCreationDtos = orderCreationDto.orderDetails();
		List<OrderDetail> orderDetails = new ArrayList<>();
		
		Order order = orderMapper.toEntity(orderCreationDto);
		// Orders can only be created with state PENDING
		order.setState(OrderState.PENDING);
		
		for (OrderDetailCreationDto orderDetailCreationDto : orderDetailCreationDtos) {

			// Check that the model exists, return error if not
			ModelDto modelDto = productClient.getModel(orderDetailCreationDto.modelId());
			if (modelDto == null) {
				throw new IllegalArgumentException(
						String.format("No se ha podido procesar el pedido debido a que el modelo con referencia '%s' no está disponible. Lamentamos las molestias.", orderDetailCreationDto.modelId()));
			}
			
			// Check that the product exists, return error if not
			ProductDto productDto = productClient.getProduct(modelDto.productId());
			if (productDto == null) {
				throw new IllegalArgumentException(String.format(
						"No se ha podido procesar el pedido debido a que el producto con referencia '%s' no está disponible. Lamentamos las molestias.",
						modelDto.productId()));
			}	
			
			// Check that there is enough stock for the model, return error if not with the model id and the available stock
			Long totalStock =  inventoryClient.getTotalStock(orderDetailCreationDto.modelId());
			if (totalStock < orderDetailCreationDto.quantity()) {
				throw new IllegalArgumentException(String.format("No hay suficiente stock disponible. El stock disponible para el modelo con referencia '%s' es: '%s'.", orderDetailCreationDto.modelId(), totalStock));
			}
			
			// For now, the warehouse with id 1 will be used as default warehouse to order from
			StockDto stockDto = inventoryClient.getStock(orderDetailCreationDto.modelId(), 1L);
			if (stockDto == null) {
				throw new IllegalArgumentException(
						String.format("No hay stock disponible del modelo con referencia '%s'.",
								orderDetailCreationDto.modelId()));
			}
			
			// Update stock accordingly to the order
			StockCreationDto stockCreationDto = StockCreationDto.builder()
								.modelId(orderDetailCreationDto.modelId())
								.quantity(stockDto.quantity() - orderDetailCreationDto.quantity())
								.warehouseId(1L)
								.build();
			
			StockDto updatedStockDto = inventoryClient.updateStock(stockDto.id(), stockCreationDto);
			
			if (updatedStockDto == null) {
				throw new IllegalArgumentException(
						String.format("No se han podido reservar las unidades solicitadas del modelo con referencia '%s'. No se ha podido procesar el pedido con éxito. Por favor, inténtelo de nuevo y si el problema persiste elimine el modelo de su pedido. Lamentamos las molestias.",
								orderDetailCreationDto.modelId()));
			}
			
			// Create order detail once stock has been updated successfully
			OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailCreationDto, order);
			
			// Add sellerId to order detail
			orderDetail.setSellerId(productDto.sellerId());
			
			orderDetails.add(orderDetail);
		}
		
		// Persist order and order details
		order = orderRepository.save(order);
		orderDetails = orderDetailRepository.saveAll(orderDetails);
		
		OrderDto orderDto = orderMapper.toDto(order, orderDetails);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(orderDto);
	}

	@Override
	public ResponseEntity<List<OrderDto>> getOrdersByUserId(String userId) {
		List<Order> orders = orderRepository.findByUserId(userId);
		List<OrderDto> orderDtos = new ArrayList<>();
		
		if (!orders.isEmpty()) {
			orderDtos = orders.stream().map(orderMapper::toDto).toList();
		}
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(orderDtos);
	}

	@Override
	public ResponseEntity<OrderDto> cancelOrder(Long orderId) {
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		Order order = orderOptional.get();
		order.setState(OrderState.CANCELED);
		order = orderRepository.save(order);
		OrderDto orderDto = orderMapper.toDto(order);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(orderDto);
	}

	@Override
	public ResponseEntity<OrderDto> confirmOrder(Long orderId) {
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		Order order = orderOptional.get();
		order.setState(OrderState.CONFIRMED);
		order = orderRepository.save(order);
		OrderDto orderDto = orderMapper.toDto(order);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(orderDto);
	}

	@Override
	public ResponseEntity<Page<OrderDto>> getFilteredOrders(
		String userId,
		OrderState state, 
		Integer month, 
		Integer year, 
		Integer page,
		Integer size, 
		String sort
	) {
		// Filter Dto
		OrderFilterDto orderFilterDto = OrderFilterDto.builder()
					                .userId(userId)
									.state(state)
									.month(month)
									.year(year)
									.build();
		
		// Parse and create sort orders
        List<SortDto> sortDtos = jsonStringToSortDto(sort);
        List<Sort.Order> sortOrders = new ArrayList<>();
        
        if (sortDtos != null) {
            for(SortDto sortDto: sortDtos) {
                Sort.Direction direction = 
                	Objects.equals(sortDto.direction(), "desc") 
                	? Sort.Direction.DESC 
                	: Sort.Direction.ASC;
                sortOrders.add(new Sort.Order(direction, sortDto.field()));
            }
        }
        
        // Create page request with sorting
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortOrders));
        
        // Apply specification and pagination
        Specification<Order> specification = OrderSpecification.getSpecification(orderFilterDto);
        Page<Order> orders = orderRepository.findAll(specification, pageRequest);
        
        // Mapp to Dto 
        Page<OrderDto> orderDtos = orders.map(orderMapper::toDto);
		
		return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDtos);
	}
	
	private List<SortDto> jsonStringToSortDto(String jsonString) {
        ObjectMapper obj = new ObjectMapper();
        try {
			return obj.readValue(jsonString, new TypeReference<>() {});
		} catch (Exception e) {
            throw new IllegalArgumentException("Invalid sort parameter");
        }
    }

	@Override
	public ResponseEntity<OrderDto> getOrderById(Long orderId) {
		Optional<Order> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isPresent()) {
			OrderDto orderDto = orderMapper.toDto(orderOptional.get());
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(orderDto);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
