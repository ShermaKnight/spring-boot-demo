package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    @Override
    public Date getInformation() {
        return new Date();
    }

    @Override
    public Date getPermission() {
        return new Date();
    }
}
