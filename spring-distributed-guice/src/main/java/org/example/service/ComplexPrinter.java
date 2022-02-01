package org.example.service;

import com.google.inject.Singleton;

@Singleton
public class ComplexPrinter implements Printer {

    @Override
    public String print() {
        return this.getClass().getName();
    }
}
