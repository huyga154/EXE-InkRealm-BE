package fe.be_inkrealm_demo2.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DebugPrinter implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("----");
        System.out.println("Datasource URL: " + System.getProperty("spring.datasource.url"));
        System.out.println("----");
    }
}

