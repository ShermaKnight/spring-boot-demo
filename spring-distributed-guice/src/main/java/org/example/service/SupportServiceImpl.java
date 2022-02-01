package org.example.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.collections4.CollectionUtils;
import org.example.annotation.Log;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class SupportServiceImpl implements SupportService {

    @Inject
    @Named("simplePrinter")
    private Printer printer;

    @Inject
    @Named("currency")
    private List<String> currency;

    @Override
    @Log("执行注入方法")
    public void execute() {
        System.out.println(printer.print());
        if (CollectionUtils.isNotEmpty(currency)) {
            System.out.println(currency.stream().collect(Collectors.joining(", ")));
        }
    }

    @Inject
    private Map<String, Printer> printerMap;

    @Override
    public void printByMap() {
        printerMap.keySet().stream().forEach(key -> {
            System.out.println(key + ": " + printerMap.get(key).print());
        });
        System.out.println();
    }

    @Inject
    private Set<Printer> printers;

    @Override
    public void printBySet() {
        System.out.println(printers.stream().map(Printer::print).collect(Collectors.joining(", ")));
        System.out.println();
    }
}
