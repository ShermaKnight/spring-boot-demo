package org.example;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.example.config.ServerModule;
import org.example.service.SupportService;

@Singleton
public class Application {

    @Inject
    @Named("supportService")
    private SupportService supportService;

    public SupportService getSupportService() {
        return supportService;
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ServerModule());
        Application application = injector.getInstance(Application.class);
        SupportService supportService = application.getSupportService();
        supportService.execute();
        supportService.printByMap();
        supportService.printBySet();
    }
}
