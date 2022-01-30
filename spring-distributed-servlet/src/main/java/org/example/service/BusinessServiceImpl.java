package org.example.service;

import org.example.domain.vo.BusinessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    @Override
    public BusinessResponse getInformation(HttpServletRequest servletRequest) {
        return build(servletRequest);
    }

    private BusinessResponse build(HttpServletRequest servletRequest) {
        BusinessResponse response = new BusinessResponse(new Date());
        response.setPath(servletRequest.getServletPath());
        return response;
    }
}
