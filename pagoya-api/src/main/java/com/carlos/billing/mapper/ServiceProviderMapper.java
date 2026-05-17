package com.carlos.billing.mapper;

import com.carlos.billing.dto.ServiceProviderResponse;
import com.carlos.billing.model.ServiceProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceProviderMapper {
    @Mapping(target = "category", expression = "java(p.getCategory().name())")
    ServiceProviderResponse toResponse(ServiceProvider p);
}
