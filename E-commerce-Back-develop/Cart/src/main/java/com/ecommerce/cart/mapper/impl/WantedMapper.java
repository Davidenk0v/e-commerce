package com.ecommerce.cart.mapper.impl;

import com.ecommerce.cart.dto.response.WantedDto;
import com.ecommerce.cart.entities.Wanted;
import com.ecommerce.cart.mapper.IDtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WantedMapper implements IDtoMapper<Wanted, WantedDto> {
    @Override
    public Wanted dtoToEntity(WantedDto dto) {
        return Wanted.builder()
                .id(dto.id())
                .modelId(dto.modelId())
                .userId(dto.userId())
                .build();
    }

    @Override
    public WantedDto entityToDto(Wanted entity) {
        return WantedDto.builder()
                .id(entity.getId())
                .modelId(entity.getModelId())
                .userId(entity.getUserId())
                .build();
    }

    @Override
    public List<WantedDto> entityListToResponseDtoList(List<Wanted> entityList) {
        return entityList.stream().map(this::entityToDto).toList();
    }
}
