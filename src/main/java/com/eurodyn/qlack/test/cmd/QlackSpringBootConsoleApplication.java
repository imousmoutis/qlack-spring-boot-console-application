package com.eurodyn.qlack.test.cmd;

import com.eurodyn.qlack.test.cmd.services.aaa.UserServiceTest;
import com.eurodyn.qlack.test.cmd.services.audit.AuditLevelServiceTest;
import com.eurodyn.qlack.test.cmd.services.audit.AuditServiceTest;
import com.eurodyn.qlack.test.cmd.services.lexicon.KeyServiceTest;
import com.eurodyn.qlack.test.cmd.services.lexicon.LanguageServiceTest;
import com.eurodyn.qlack.test.cmd.services.mailing.InternalMessageServiceTest;
import com.eurodyn.qlack.test.cmd.services.mailing.MailServiceTest;
import com.eurodyn.qlack.test.cmd.services.settings.SettingsServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories({
        "com.eurodyn.qlack.fuse.aaa.repository",
        "com.eurodyn.qlack.fuse.audit.repository",
        "com.eurodyn.qlack.fuse.lexicon.repository",
        "com.eurodyn.qlack.fuse.mailing.repository",
        "com.eurodyn.qlack.fuse.settings.repository"
})
@EntityScan({
        "com.eurodyn.qlack.fuse.aaa.model",
        "com.eurodyn.qlack.fuse.audit.model",
        "com.eurodyn.qlack.fuse.lexicon.model",
        "com.eurodyn.qlack.fuse.mailing.model",
        "com.eurodyn.qlack.fuse.settings.model"
})
@ComponentScan(basePackages = {
        "com.eurodyn.qlack.test.cmd.services",
        "com.eurodyn.qlack.fuse.aaa",
        "com.eurodyn.qlack.fuse.audit",
        "com.eurodyn.qlack.fuse.lexicon",
        "com.eurodyn.qlack.fuse.mailing",
        "com.eurodyn.qlack.fuse.settings"
})
public class QlackSpringBootConsoleApplication implements CommandLineRunner {

    @Autowired
    private UserServiceTest userServiceTest;

    @Autowired
    private AuditServiceTest auditServiceTest;

    @Autowired
    private AuditLevelServiceTest auditLevelServiceTest;

    @Autowired
    private SettingsServiceTest settingsServiceTest;

    @Autowired
    private MailServiceTest mailServiceTest;

    @Autowired
    private InternalMessageServiceTest internalMessageServiceTest;

    @Autowired
    private LanguageServiceTest languageServiceTest;

    @Autowired
    private KeyServiceTest keyServiceTest;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(QlackSpringBootConsoleApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    public void run(String... args) {

        if (args.length == 0){
            System.out.println("Please provide the service name you want to test as the first argument.");
        } else{

            switch (args[0]){
                case "UserService":
                    userServiceTest.createUser();
                    userServiceTest.updateUser();
                    userServiceTest.getUserByName();
                    userServiceTest.deleteUser();
                    break;
                case "AuditService":
                    auditLevelServiceTest.addLevelIfNotExists();
                    auditLevelServiceTest.listAuditLevels();
                    auditLevelServiceTest.getAuditLevelByName();
                    auditServiceTest.audit();
                    auditServiceTest.getAuditLogs();
                    break;
                case "SettingsService":
                    settingsServiceTest.createSetting();
                    settingsServiceTest.getSettings();
                    break;
                case "MailService":
                    mailServiceTest.queueEmail();
                    internalMessageServiceTest.sendInternalMail();
                    break;
                case "LanguageService":
                    languageServiceTest.createLanguageIfNotExists();
                    languageServiceTest.downloadLanguage(languageServiceTest.getLanguage().getId());
                    languageServiceTest.deactivateLanguage();
                    languageServiceTest.getLanguages();
                    keyServiceTest.createKey();
                    keyServiceTest.getTranslationsForLocale();
                    break;
                default: System.out.println("Service " +args[0]+ " is not found :(");
            }
        }
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}