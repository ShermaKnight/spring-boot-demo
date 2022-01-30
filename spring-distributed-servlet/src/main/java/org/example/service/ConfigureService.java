package org.example.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ConfigureService {

    void getConfigure(HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    void configureChanged(String serviceName, List<String> data);
}
