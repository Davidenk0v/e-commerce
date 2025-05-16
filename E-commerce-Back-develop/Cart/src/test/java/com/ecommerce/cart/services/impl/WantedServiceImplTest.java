package com.ecommerce.cart.services.impl;

import com.ecommerce.cart.dto.response.WantedDto;
import com.ecommerce.cart.entities.ProductDetails;
import com.ecommerce.cart.entities.Wanted;
import com.ecommerce.cart.mapper.IDtoMapper;
import com.ecommerce.cart.repositories.ProductDetailsRepository;
import com.ecommerce.cart.repositories.WantedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
/*
class WantedServiceImplTest {

    @Mock
    private ProductDetailsRepository productDetailsRepository;

    @Mock
    private WantedRepository wantedRepository;

    @Mock
    private IDtoMapper<Wanted, WantedDto> dtoMapper;

    @InjectMocks
    private WantedServiceImpl wantedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToWantedList_Success() {
        Long productId = 1L;
        String userId = "user1";
        ProductDetails productDetails = new ProductDetails();
        Wanted wanted = Wanted.builder().userId(userId).productId(productId).build();
        WantedDto wantedDto = dtoMapper.entityToDto(wanted);

        when(productDetailsRepository.findById(productId)).thenReturn(Optional.of(productDetails));
        when(wantedRepository.save(any(Wanted.class))).thenReturn(wanted);
        when(dtoMapper.entityToDto(wanted)).thenReturn(wantedDto);

        ResponseEntity<WantedDto> response = wantedService.addToWantedList(productId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wantedDto, response.getBody());
        verify(wantedRepository).save(any(Wanted.class));
    }

    @Test
    void addToWantedList_ProductNotFound() {
        Long productId = 1L;
        String userId = "user1";

        when(productDetailsRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> wantedService.addToWantedList(productId, userId));
    }

    @Test
    void addToWantedList_SaveError() {
        Long productId = 1L;
        String userId = "user1";
        ProductDetails productDetails = new ProductDetails();

        when(productDetailsRepository.findById(productId)).thenReturn(Optional.of(productDetails));
        when(wantedRepository.save(any(Wanted.class))).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> wantedService.addToWantedList(productId, userId));
    }

    @Test
    void removeFromWantedList_Success() {
        Long productId = 1L;
        String userId = "user1";
        Wanted wanted = Wanted.builder().userId(userId).productId(productId).build();
        WantedDto wantedDto = dtoMapper.entityToDto(wanted);

        when(wantedRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(wanted));
        when(dtoMapper.entityToDto(wanted)).thenReturn(wantedDto);

        ResponseEntity<WantedDto> response = wantedService.removeFromWantedList(productId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wantedDto, response.getBody());
        verify(wantedRepository).delete(wanted);
    }

    @Test
    void removeFromWantedList_NotFound() {
        Long productId = 1L;
        String userId = "user1";

        when(wantedRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> wantedService.removeFromWantedList(productId, userId));
    }

    @Test
    void removeFromWantedList_DeleteError() {
        Long productId = 1L;
        String userId = "user1";
        Wanted wanted = Wanted.builder().userId(userId).productId(productId).build();

        when(wantedRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(wanted));
        doThrow(new IllegalArgumentException()).when(wantedRepository).delete(wanted);

        assertThrows(IllegalArgumentException.class, () -> wantedService.removeFromWantedList(productId, userId));
    }

    @Test
    void clearWantedList_Success() {
        String userId = "user1";
        List<Wanted> wantedList = Arrays.asList(new Wanted(), new Wanted());

        when(wantedRepository.findByUserId(userId)).thenReturn(wantedList);

        ResponseEntity<String> response = wantedService.clearWantedList(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Wanted list cleared", response.getBody());
        verify(wantedRepository).deleteAll(wantedList);
    }

    @Test
    void clearWantedList_EmptyList() {
        String userId = "user1";

        when(wantedRepository.findByUserId(userId)).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> wantedService.clearWantedList(userId));
    }

    @Test
    void clearWantedList_DeleteError() {
        String userId = "user1";
        List<Wanted> wantedList = Arrays.asList(new Wanted(), new Wanted());

        when(wantedRepository.findByUserId(userId)).thenReturn(wantedList);
        doThrow(new IllegalArgumentException()).when(wantedRepository).deleteAll(wantedList);

        assertThrows(IllegalArgumentException.class, () -> wantedService.clearWantedList(userId));
    }


    @Test
    void getWantedList_EmptyList() {
        String userId = "user1";

        when(wantedRepository.findByUserId(userId)).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> wantedService.getWantedList(userId));
    }

    @Test
    void newWantedList_Success() {
        String userId = "user1";
        Wanted wanted = Wanted.builder().userId(userId).productId(null).build();

        when(wantedRepository.save(any(Wanted.class))).thenReturn(wanted);

        ResponseEntity<Wanted> response = wantedService.newWantedList(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(wanted, response.getBody());
        verify(wantedRepository).save(any(Wanted.class));
    }

    @Test
    void newWantedList_SaveError() {
        String userId = "user1";

        when(wantedRepository.save(any(Wanted.class))).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> wantedService.newWantedList(userId));
    }
}
*/