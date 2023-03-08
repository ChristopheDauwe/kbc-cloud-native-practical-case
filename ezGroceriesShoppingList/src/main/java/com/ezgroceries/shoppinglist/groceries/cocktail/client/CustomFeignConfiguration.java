package com.ezgroceries.shoppinglist.groceries.cocktail.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignConfiguration {

    @Bean
    public void Config() {
        System.setProperty("javax.net.ssl.keyStore", "/Users/christophedauwe/Library/Java/JavaVirtualMachines/openjdk-19.0.2/Contents/Home/lib/security/cacerts");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
    }
}