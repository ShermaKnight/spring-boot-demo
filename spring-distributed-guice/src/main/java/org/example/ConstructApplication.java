package org.example;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.example.config.ServerModule;
import org.example.service.Printer;

@Singleton
public class ConstructApplication {

    private Printer printer;

    @Inject
    public ConstructApplication(@Named("simplePrinter") Printer printer) {
        this.printer = printer;
    }

    public void print() {
        System.out.println(printer.print());
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ServerModule());
        ConstructApplication application = injector.getInstance(ConstructApplication.class);
        application.print();
    }
}
