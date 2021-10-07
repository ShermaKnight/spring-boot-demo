package org.example.service;

import org.example.model.BusinessResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface BusinessService {

    BusinessResponse getBusinessById(Long id);
}
