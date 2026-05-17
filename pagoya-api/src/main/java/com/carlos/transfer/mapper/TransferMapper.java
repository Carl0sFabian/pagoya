package com.carlos.transfer.mapper;

import com.carlos.transfer.dto.TransferResponse;
import com.carlos.transfer.model.Transfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferResponse toResponse(Transfer transfer);
}