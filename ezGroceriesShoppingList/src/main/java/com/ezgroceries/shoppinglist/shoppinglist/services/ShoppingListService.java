package com.ezgroceries.shoppinglist.shoppinglist.services;

import com.ezgroceries.shoppinglist.groceries.cocktail.Cocktail;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingList;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("ShoppingListService")
public class ShoppingListService {

    private static final UUID DUMMY_SHOPPINGLIST = UUID.randomUUID();
    private final ShoppingList STEPHANIE = new ShoppingList("Stephanie's birthday",DUMMY_SHOPPINGLIST,List.of("Tequila",
            "Triple sec",
            "Lime juice",
            "Salt",
            "Blue Curacao"));

    private final ShoppingList MY_BD = new ShoppingList("My Birthday",DUMMY_SHOPPINGLIST,List.of("Tequila",
            "Triple sec",
            "Lime juice",
            "Salt",
            "Blue Curacao"));
    public ShoppingList createShoppingList(ShoppingList shoppingList) {
        shoppingList.setId(DUMMY_SHOPPINGLIST);
        return shoppingList;
    }


    public ShoppingList addCocktail(ShoppingList shoppingList, Cocktail cocktail) {
        return STEPHANIE;
    }

    public Optional<ShoppingList> findById(UUID id) {
        return Optional.of(STEPHANIE);
    }

    public Collection<ShoppingList> findAll() {
        return List.of(STEPHANIE,MY_BD);
    }

}
