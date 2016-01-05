package com.balaji.goeuro;

import com.balaji.goeuro.service.PositionService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Main {

    @Autowired
    PositionService positionService;

    @Bean
    public ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static void main(String[] args) {
        if(args.length == 0) {
            System.out.println("Please enter a query.\nFor e.g. $> java -jar GoEuroTest.jar \"Berlin\"");
            return;
        }
        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
        Main main = applicationContext.getBean(Main.class);

        boolean result = main.positionService.createCSVForPositionData(args[0]);
        if(result) {
            System.out.println("CSV file created. Please check `output.csv` in current directory");
        } else {
            System.out.println("Error in creating CSV file.");
        }
    }
}
