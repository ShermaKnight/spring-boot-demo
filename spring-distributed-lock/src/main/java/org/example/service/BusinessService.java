package org.example.service;

import org.example.domain.vo.BusinessResponse;

import javax.servlet.http.HttpServletRequest;

public interface BusinessService {

    BusinessResponse getInformation(HttpServletRequest servletRequest);

    void decrease();
}
