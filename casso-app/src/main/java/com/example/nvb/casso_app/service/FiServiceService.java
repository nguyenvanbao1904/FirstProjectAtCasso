package com.example.nvb.casso_app.service;

import com.example.nvb.casso_app.dto.request.FiServiceRequest;
import com.example.nvb.casso_app.dto.response.FiServiceResponse;

public interface FiServiceService {
    FiServiceResponse.FiServiceItem saveFiService(FiServiceRequest fiServiceRequest);
    long countService();
    FiServiceResponse getFiServices();
    FiServiceResponse.FiServiceItem getFiService(String id);
    FiServiceResponse getMyFiServices();
}
