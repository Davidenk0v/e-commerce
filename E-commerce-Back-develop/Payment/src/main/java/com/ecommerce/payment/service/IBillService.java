package com.ecommerce.payment.service;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import com.ecommerce.payment.dto.response.BillDto;

public interface IBillService {

	ResponseEntity<BillDto> getBillByOrderId(Long orderId);
	
	ResponseEntity<byte[]> generateBillPdf(Long billId);
	
	ResponseEntity<ByteArrayResource> generateBillCsv(Long billId) throws IOException;
}
