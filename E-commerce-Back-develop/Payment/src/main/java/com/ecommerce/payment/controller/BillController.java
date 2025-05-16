package com.ecommerce.payment.controller;

import java.io.IOException;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.dto.response.BillDto;
import com.ecommerce.payment.service.IBillService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment/bill")
@RequiredArgsConstructor
@Tag(name = "Bill", description = "Operations related to bills")
public class BillController {

	private final IBillService billService;
	
	@GetMapping("/for-order/{orderId}")
	public ResponseEntity<BillDto> getBillByOrderId(@PathVariable Long orderId) {
		return billService.getBillByOrderId(orderId);
	}
	
	@GetMapping("/pdf/{billId}")
	public ResponseEntity<byte[]> generateBillPdf(@PathVariable Long billId) {
		// Fijar locale a España
		LocaleContextHolder.setLocale(Locale.forLanguageTag("es-ES"));
	    
        return billService.generateBillPdf(billId);
    }
	
	@GetMapping("/csv/{billId}")
	public ResponseEntity<ByteArrayResource> generateBillCsv(@PathVariable Long billId) throws IOException {
		return billService.generateBillCsv(billId);
	}
	
}
