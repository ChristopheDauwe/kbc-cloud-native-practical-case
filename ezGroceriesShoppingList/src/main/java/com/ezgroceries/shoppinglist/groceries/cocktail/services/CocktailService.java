package com.ezgroceries.shoppinglist.groceries.cocktail.services;

import com.ezgroceries.shoppinglist.groceries.cocktail.Cocktail;
import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailMapper;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBResponse;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("CocktailService")
public class CocktailService {

    private final CocktailDBClient cocktailClient;
    private final CocktailMapper cocktailMapper;


    public CocktailService(CocktailDBClient cocktailClient, CocktailMapper cocktailMapper) {
        this.cocktailClient = cocktailClient;
        this.cocktailMapper = cocktailMapper;
    }

    public Collection<Cocktail> findAll(String search){
        CocktailDBResponse response = cocktailClient.searchCocktails(search);

        return response.getDrinks().stream().map(cocktailMapper::toCocktail).toList();
    }
}
