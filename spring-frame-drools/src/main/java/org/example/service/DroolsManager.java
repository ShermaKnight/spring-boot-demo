package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.example.domain.dto.DroolsRule;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.internal.utils.KieService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class DroolsManager {

    private final KieServices kieServices = KieServices.get();
    private final KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    private final KieModuleModel kieModuleModel = kieServices.newKieModuleModel();

    private KieContainer kieContainer;

    public boolean existsKieBase(String kieBaseName) {
        if (null == kieContainer) {
            return false;
        }
        Collection<String> kieBaseNames = kieContainer.getKieBaseNames();
        if (kieBaseNames.contains(kieBaseName)) {
            return true;
        }
        return false;
    }

    public void deleteDroolsRule(String kieBaseName, String packageName, String ruleName) {
        if (existsKieBase(kieBaseName)) {
            KieBase kieBase = kieContainer.getKieBase(kieBaseName);
            kieBase.removeRule(packageName, ruleName);
        }
    }

    public void addOrUpdateRule(DroolsRule droolsRule) {
        String kieBaseName = droolsRule.getKieBaseName();
        boolean existsKieBase = existsKieBase(kieBaseName);
        KieBaseModel kieBaseModel = null;
        if (!existsKieBase) {
            kieBaseModel = kieModuleModel.newKieBaseModel(kieBaseName);
            kieBaseModel.setDefault(false);
            kieBaseModel.addPackage(droolsRule.getKiePackageName());
            kieBaseModel.newKieSessionModel(kieBaseName + "-session").setDefault(false);
        } else {
            kieBaseModel = kieModuleModel.getKieBaseModels().get(kieBaseName);
            List<String> packages = kieBaseModel.getPackages();
            if (!packages.contains(droolsRule.getKiePackageName())) {
                kieBaseModel.addPackage(droolsRule.getKiePackageName());
            } else {
                kieBaseModel = null;
            }
        }
        String file = "src/main/resources/" + droolsRule.getKiePackageName() + "/" + droolsRule.getRuleId() + ".drl";
        kieFileSystem.write(file, droolsRule.getRuleContent());
        if (kieBaseModel != null) {
            String xml = kieModuleModel.toXML();
            kieFileSystem.writeKModuleXML(xml);
        }

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        Results results = kieBuilder.getResults();
        List<Message> messages = results.getMessages(Message.Level.ERROR);
        if (null != messages && !messages.isEmpty()) {
            for (Message message : messages) {
                log.error(message.getText());
            }
            throw new RuntimeException("Load rules failed.");
        }
        if (null == kieContainer) {
            kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        } else {
            ((KieContainerImpl) kieContainer).updateToKieModule((InternalKieModule) kieBuilder.getKieModule());
        }
    }

    public String fireRule(String kieBaseName, Integer param) {
        KieSession kieSession = kieContainer.newKieSession(kieBaseName + "-session");
        StringBuilder resultInfo = new StringBuilder();
        kieSession.setGlobal("resultInfo", resultInfo);
        kieSession.insert(param);
        kieSession.fireAllRules();
        kieSession.dispose();
        return resultInfo.toString();
    }
}
