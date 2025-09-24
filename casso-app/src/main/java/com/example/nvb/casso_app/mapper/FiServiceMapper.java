package com.example.nvb.casso_app.mapper;

import com.example.nvb.casso_app.dto.request.FiServiceRequest;
import com.example.nvb.casso_app.dto.response.FiServiceResponse;
import com.example.nvb.casso_app.entity.FiService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FiServiceMapper {
    FiService toEntity(FiServiceRequest request);
    FiService toEntity (FiServiceResponse.FiServiceItem response);
    FiServiceResponse.FiServiceItem toResponse(FiService entity);
    FiServiceRequest toRequest(FiServiceResponse.FiServiceItem response);
}
