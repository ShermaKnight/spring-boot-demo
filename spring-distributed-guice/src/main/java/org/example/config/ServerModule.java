package org.example.config;

import cn.hutool.core.map.MapBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.example.annotation.Log;
import org.example.interceptor.LogInterceptor;
import org.example.service.*;

import java.util.Arrays;
import java.util.List;

public class ServerModule extends AbstractModule {

    @Override
    protected void configure() {
        // 接口绑定
        bind(Printer.class).annotatedWith(Names.named("simplePrinter")).to(SimplePrinter.class);
        bind(Printer.class).annotatedWith(Names.named("complexPrinter")).to(ComplexPrinter.class);
        bind(SupportService.class).annotatedWith(Names.named("supportService")).to(SupportServiceImpl.class);

        // 泛型绑定
        bind(new TypeLiteral<List<String>>() {
        }).annotatedWith(Names.named("currency")).toInstance(Arrays.asList("CNY", "ENR", "USD"));

        // map绑定
        MapBinder<String, Printer> printerMap = MapBinder.newMapBinder(binder(), String.class, Printer.class);
        printerMap.addBinding("simplePrinter").to(SimplePrinter.class);
        printerMap.addBinding("complexPrinter").to(ComplexPrinter.class);

        // set绑定
        Multibinder<Printer> printers = Multibinder.newSetBinder(binder(), Printer.class);
        printers.addBinding().to(SimplePrinter.class);
        printers.addBinding().to(ComplexPrinter.class);

        // 拦截器
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Log.class), new LogInterceptor());
    }
}
