package com.ezgroceries.shoppinglist.shoppinglist.services;

import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailException;
import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import com.ezgroceries.shoppinglist.groceries.cocktail.services.CocktailService;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListException;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListMapper;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.shoppinglist.domain.ShoppingList;
import com.ezgroceries.shoppinglist.shoppinglist.repo.ShoppingListRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ShoppingListService")
public class ShoppingListService {


    private final ShoppingListRepository shoppingListRepository;
    private final CocktailService cocktailService;

    private final ShoppingListMapper shoppingListMapper;

    public ShoppingListService(ShoppingListRepository shoppingListRepository, CocktailService cocktailService, ShoppingListMapper shoppingListMapper) {
        this.shoppingListRepository = shoppingListRepository;
        this.cocktailService = cocktailService;
        this.shoppingListMapper = shoppingListMapper;
    }

    public ShoppingListResource createShoppingList(ShoppingListResource shoppingListResource) {

        ShoppingList shoppingList = shoppingListRepository.save(new ShoppingList(UUID.randomUUID(), shoppingListResource.name(), Set.of()));
        return new ShoppingListResource(shoppingList.getId(), shoppingList.getName(), List.of());
    }


    public ShoppingListResource addCocktail(ShoppingListResource shoppingListResource, CocktailResource cocktailResource) {

        Cocktail cocktail = cocktailService.findCocktailById(cocktailResource.id());


        ShoppingList list = findShoppingListById(shoppingListResource.id());
        list.addCocktail(cocktail);
        shoppingListRepository.save(list);

        return shoppingListMapper.toShoppingListResource(list,getIngredients(list));


    }

    private ShoppingList findShoppingListById(UUID uuid) {
        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(uuid);
        return shoppingList.orElseThrow(() -> new ShoppingListException(String.format("Shopping list with id %s does not exist", uuid)));
    }

    public Collection<ShoppingList> findAllInDB() {
        return shoppingListRepository.findAll();
    }

    public ShoppingListResource findById(UUID uuid) {
        ShoppingList shoppingList = findShoppingListById(uuid);
        return shoppingListMapper.toShoppingListResource(shoppingList,getIngredients(shoppingList));
    }

    private Collection<String> getIngredients(ShoppingList list){
        Collection<String> ingredients = new ArrayList<>();

        list.getCocktails().forEach(cocktail -> ingredients.addAll(cocktail.getIngredients()));

        return ingredients;
    }


    public Collection<ShoppingListResource> findAll() {
        return shoppingListRepository.findAll().stream().map(shoppingList -> shoppingListMapper.toShoppingListResource(shoppingList,getIngredients(shoppingList))).toList();
    }


}
