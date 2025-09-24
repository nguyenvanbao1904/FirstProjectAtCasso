package com.example.nvb.casso_app.service.impl;

import com.example.nvb.casso_app.dto.request.FiServiceRequest;
import com.example.nvb.casso_app.dto.response.FiServiceResponse;
import com.example.nvb.casso_app.entity.FiService;
import com.example.nvb.casso_app.mapper.FiServiceMapper;
import com.example.nvb.casso_app.repository.FiServiceRepository;
import com.example.nvb.casso_app.service.FiServiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class FiServiceServiceImpl implements FiServiceService {
    FiServiceRepository fiServiceRepository;
    FiServiceMapper fiServiceMapper;
    @Override
    public FiServiceResponse.FiServiceItem saveFiService(FiServiceRequest fiServiceRequest) {
        FiService fiService = fiServiceMapper.toEntity(fiServiceRequest);
        return fiServiceMapper.toResponse(fiServiceRepository.save(fiService));
    }

    @Override
    public long countService() {
        return fiServiceRepository.count();
    }

    @Override
    public FiServiceResponse getFiServices() {
        List<FiServiceResponse.FiServiceItem> fiServiceResponseList = fiServiceRepository.findAllNotLinked()
                .stream()
                .map(fiServiceMapper::toResponse)
                .toList();

        return FiServiceResponse.builder()
                .fiServices(fiServiceResponseList)
                .build();
    }

    @Override
    public FiServiceResponse.FiServiceItem getFiService(String id) {
        FiService fiService = fiServiceRepository.findById(id).orElse(null);
        return fiServiceMapper.toResponse(fiService);
    }

    @Override
    public FiServiceResponse getMyFiServices() {
        List<FiServiceResponse.FiServiceItem> myFiServiceResponseList = fiServiceRepository.findAllLinked()
                .stream()
                .map(fiServiceMapper::toResponse)
                .toList();

        return FiServiceResponse.builder()
                .fiServices(myFiServiceResponseList)
                .build();
    }
}
