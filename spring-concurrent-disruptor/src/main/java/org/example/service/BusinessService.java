package org.example.service;

import org.example.domain.dto.Athlete;
import org.example.domain.dto.Referee;
import org.example.domain.vo.BusinessResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BusinessService {

    BusinessResponse getInformation(HttpServletRequest servletRequest);

    void running(List<Athlete> athletes, Referee referee);
}
