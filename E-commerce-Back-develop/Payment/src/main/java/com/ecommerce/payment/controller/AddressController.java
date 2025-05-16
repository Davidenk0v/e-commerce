package com.ecommerce.payment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.payment.dto.request.AddressCreationDto;
import com.ecommerce.payment.dto.response.AddressDto;
import com.ecommerce.payment.dto.response.ErrorResponseDto;
import com.ecommerce.payment.service.IAddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment/address")
@RequiredArgsConstructor
@Tag(name = "Address", description = "Operations related to addresses")
public class AddressController {

	private final IAddressService addressService;
	
	@ApiResponses(value = {
        @ApiResponse(
        	responseCode = "201", 
			description="Address created successfully"),
        @ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Address with the alias 'Casa' already exists for the user with id '99606b3e-1b1f-4451-ad6d-8041c2aa4925'\",\"details\":\"uri=/api/v1/payment/address\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
    @Operation(
    	summary = "Create a new address",
    	description = "Creates an address after checking that the user does not have and address with the same alias")
	@PostMapping
	public ResponseEntity<AddressDto> saveAddress(@RequestBody AddressCreationDto addressCreationDto) {
		return addressService.saveAddress(addressCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "204", 
			description = "Address deleted"),
		@ApiResponse(
			responseCode = "404", 
			description = "Address not found")})
	@Operation(
		summary = "Delete an address by id", 
		description = "Deletes the address with the given id")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
		return addressService.deleteAddress(id);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Address updated"),
		@ApiResponse(
			responseCode = "404", 
			description = "Address not found",
			content = {
				@Content(schema = @Schema(implementation = Void.class))}),
		@ApiResponse(
        	responseCode = "400", 
        	description = "Invalid request body",
            content = {
            	@Content(
            		examples = {@ExampleObject(value = "{\"status\":\"400 BAD_REQUEST\",\"message\":\"Address with the alias 'Casa' already exists for the user with id '99606b3e-1b1f-4451-ad6d-8041c2aa4925'\",\"details\":\"uri=/api/v1/payment/address\"}")},
            		mediaType = "application/json",
            		schema = @Schema(implementation = ErrorResponseDto.class))})})
	@Operation(
			summary = "Updates an address by id", 
			description = "Updates the address with the given id, checking first if the new alias is available")
	@PutMapping("/{id}")
	public ResponseEntity<AddressDto> updateCategory(@PathVariable Long id, @RequestBody AddressCreationDto addressCreationDto) {
		return addressService.updateAddress(id, addressCreationDto);
	}
	
	@ApiResponses(value = { 
		@ApiResponse(
			responseCode = "200", 
			description = "Addresses found", 
			content = {@Content(examples = {
				@ExampleObject(value = "[{\"id\":1,\"title\":\"portátiles\"},{\"id\":2,\"title\":\"smartphones\"}]")})})})
	@Operation(
		summary = "Get all addresses for the given user", 
		description = "Returns all the addresses for the user with the given id")
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<AddressDto>> getAddressesByUserId(@PathVariable String userId) {
		return addressService.getAddressesByUserId(userId);
	}
}
