package com.ezgroceries.shoppinglist.groceries.cocktail.web;

import com.ezgroceries.shoppinglist.groceries.cocktail.Cocktail;
import com.ezgroceries.shoppinglist.groceries.cocktail.services.CocktailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("CocktailController")
public class CocktailController {

    private static final Logger log = LoggerFactory.getLogger(CocktailController.class);

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping(value = "/cocktails")
    @ResponseStatus(HttpStatus.OK)
    public List<Cocktail> allCocktails(@RequestParam("search") String search) {

        log.info("search: {}",search);
        return cocktailService.findAll();
    }

}
