package com.ecommerce.payment.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ecommerce.payment.dto.response.BestSellerProductDto;
import com.ecommerce.payment.dto.response.SalesResultDto;
import com.ecommerce.payment.repository.OrderDetailRepository;
import com.ecommerce.payment.service.ISellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerService implements ISellerService {
	
	private final OrderDetailRepository orderDetailRepository;
	
	@Override
	public ResponseEntity<List<BestSellerProductDto>> getBestSellingProducts(Integer limit, String sellerId) {
		
		List<BestSellerProductDto> bestSellerProducts = orderDetailRepository.getBestSellingProducts(sellerId, PageRequest.of(0, limit));

		return ResponseEntity.ok(bestSellerProducts);
	}

	@Override
	public ResponseEntity<SalesResultDto> getSalesResult(String sellerId) {
		
		// Get objects containing the creation date of the order and the total sales for it (date, total) for the last 12 months, current included
	    ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime startDateTime = now.minusMonths(12).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        Instant startDate = startDateTime.toInstant();
        
        List<Object[]> results = orderDetailRepository.getSalesResult(sellerId, startDate);

        BigDecimal totalSales = BigDecimal.ZERO;
        List<Long> salesByMonth = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            salesByMonth.add(0L);
        }

        for (Object[] result : results) {
            Date date = (Date) result[0];
            BigDecimal amount = (BigDecimal) result[1];

            totalSales = totalSales.add(amount);
            
            // Take into account when there are two overlapping years (most cases)
            int monthIndex = date.toLocalDate().getMonthValue() - startDateTime.getMonthValue();
            if (monthIndex < 0) monthIndex += 12;
            salesByMonth.set(monthIndex, amount.longValue());
        }

		SalesResultDto salesResult = SalesResultDto.builder()
                .totalSales(totalSales)
                .salesByMonth(salesByMonth)
                .build();
		
		return ResponseEntity.ok(salesResult);
	}

	
}
