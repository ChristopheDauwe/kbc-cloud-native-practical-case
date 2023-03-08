package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = CocktailDBClient.class)
public class EzGroceriesShoppingListApplication {

    public static void main(String[] args) {
        SpringApplication.run(EzGroceriesShoppingListApplication.class, args);
    }

}
