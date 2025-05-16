package com.ecommerce.payment.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ecommerce.payment.dto.response.BillDto;
import com.ecommerce.payment.entity.Bill;
import com.ecommerce.payment.entity.OrderDetail;
import com.ecommerce.payment.mapper.impl.BillMapper;
import com.ecommerce.payment.repository.BillRepository;
import com.ecommerce.payment.service.IBillService;
import com.opencsv.CSVWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillService implements IBillService {

	private final BillRepository billRepository;
	
	private final BillMapper billMapper;
	
	private final TemplateEngine templateEngine;
	
	@Override
	public ResponseEntity<BillDto> getBillByOrderId(Long orderId) {
		Optional<Bill> billOptional = billRepository.findByOrderId(orderId);
		
		if (!billOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Bill bill = billOptional.get();
		
		BillDto billDto = billMapper.toDto(bill);
		
		return ResponseEntity.ok(billDto);
	}

	@Override
	public ResponseEntity<byte[]> generateBillPdf(Long billId) {
		Optional<Bill> billOptional = billRepository.findById(billId);
		
		if (!billOptional.isPresent()) {
			throw new IllegalArgumentException(String.format("No se encontró la factura ref. %s", billId));
		}
		
		Bill bill = billOptional.get();
		
		// Prepare Thymeleaf context
        Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("bill", bill);
        
        // Render HTML from Thymeleaf template
        String htmlContent = templateEngine.process("bill", context);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Convert HTML to PDF
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        renderer.createPDF(baos);
        
        byte[] pdfBytes = baos.toByteArray();
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill_" + billId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
	}

	@Override
	public ResponseEntity<ByteArrayResource> generateBillCsv(Long billId) throws IOException {
		Optional<Bill> billOptional = billRepository.findById(billId);
		
		if (!billOptional.isPresent()) {
			throw new IllegalArgumentException(String.format("No se encontró la factura ref. %s", billId));
		}
		
		StringWriter stringWriter = new StringWriter();
		
		try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
			String[] billHeader = {
						"numero de factura", 
						"nombre", 
						"email",
						"tipo de tarjeta",
						"numero de tarjeta",
						"mes expiracion tarjeta",
						"año expiracion tarjeta",
						"importe recibido",
						"moneda",
						"numero de pedido",
						"fecha de expedicion",
						"calle",
						"localidad",
						"provincia",
						"cp",
						"pais"
					};
			
			String [] orderDetailHeader = {
					"referencia modelo",
					"nombre modelo",
					"precio",
					"cantidad"
			};
			
			csvWriter.writeNext(billHeader);
			
			Bill bill = billOptional.get();
			
			String[] row = {
					bill.getId().toString(), 
					bill.getName(), 
					bill.getEmail(), 
					bill.getCardBrand(), 
					bill.getCardNumber(),
					bill.getCardExpMonth().toString(), 
					bill.getCardExpYear().toString(),
					bill.getAmountReceived().toString(), 
					bill.getCurrency(), 
					bill.getOrder().getId().toString(),
					bill.getCreationDate().toString(), 
					bill.getStreet(), 
					bill.getLocality(), 
					bill.getProvince(),
					bill.getCp(), 
					bill.getCountry()
			};
			
			csvWriter.writeNext(row);
			csvWriter.writeNext(orderDetailHeader);
			
			for (OrderDetail orderDetail : bill.getOrder().getOrderDetails()) {
				String[] orderDetailRow = { 
						orderDetail.getModelId().toString(), 
						orderDetail.getName(),
						orderDetail.getPrice().toString(), 
						orderDetail.getQuantity().toString() 
				};
	
				csvWriter.writeNext(orderDetailRow);
			}
			
			String csvContent = stringWriter.toString();
			byte[] csvBytes = csvContent.getBytes();
			ByteArrayResource byteArrayResource = new ByteArrayResource(csvBytes);
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=bill_" + billId + ".csv")
					.contentType(MediaType.parseMediaType("text/csv"))
					.body(byteArrayResource);
			
		} catch (IOException e) {
			throw new IOException("Error al generar el archivo CSV", e);
		}
	}
}
