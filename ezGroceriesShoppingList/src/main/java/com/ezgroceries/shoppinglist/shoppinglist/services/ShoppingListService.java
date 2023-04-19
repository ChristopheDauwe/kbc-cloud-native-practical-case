package com.ezgroceries.shoppinglist.shoppinglist.services;

import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import com.ezgroceries.shoppinglist.groceries.cocktail.services.CocktailService;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListException;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListMapper;
import com.ezgroceries.shoppinglist.shoppinglist.domain.ShoppingList;
import com.ezgroceries.shoppinglist.shoppinglist.repo.ShoppingListRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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

    public ShoppingList createShoppingList(ShoppingList shoppingList) {

       return shoppingListRepository.save(shoppingList);
    }


    public void addCocktail(UUID shoppingListID, CocktailResource cocktailResource) {

        ShoppingList shoppingList = findById(shoppingListID);
        Cocktail cocktail = cocktailService.findCocktailById(cocktailResource.id());


        ShoppingList list = findShoppingListById(shoppingList.getId());
        list.addCocktail(cocktail);
        shoppingListRepository.save(list);

    }

    private ShoppingList findShoppingListById(UUID uuid) {
        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(uuid);
        return shoppingList.orElseThrow(() -> new ShoppingListException(String.format("Shopping list with id %s does not exist", uuid)));
    }

    public Collection<ShoppingList> findAllInDB() {
        return shoppingListRepository.findAll();
    }


    //@PostAuthorize("returnObject.username == principal.username")
    public ShoppingList findById(UUID uuid) {
        return findShoppingListById(uuid);
    }

    @PreAuthorize("#username == principal.username && hasRole('USER')")
    public Collection<ShoppingList> findAllByUsername(String username){
        return shoppingListRepository.findAllByUsername(username);
    }


    public Collection<ShoppingList> findAll() {
        return shoppingListRepository.findAll();
    }


}
