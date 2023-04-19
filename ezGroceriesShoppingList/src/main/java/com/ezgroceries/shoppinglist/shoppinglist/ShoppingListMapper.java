package com.ezgroceries.shoppinglist.shoppinglist;

import com.ezgroceries.shoppinglist.shoppinglist.domain.ShoppingList;
import org.mapstruct.Mapper;

import java.util.*;


@Mapper(componentModel = "spring")
public interface ShoppingListMapper {

    default ShoppingListResource toShoppingListResource(ShoppingList shoppingList) {

        return new ShoppingListResource(shoppingList.getId(),shoppingList.getName(),getIngredients(shoppingList));

    }

    default List<ShoppingListResource> toShoppingListResources(Collection<ShoppingList> shoppingLists) {

        return shoppingLists.stream().map(this::toShoppingListResource).toList();

    }

    private Collection<String> getIngredients(ShoppingList list){
        Collection<String> ingredients = new ArrayList<>();

        list.getCocktails().forEach(cocktail -> ingredients.addAll(cocktail.getIngredients()));

        return ingredients;
    }

    default ShoppingList toShoppingList(ShoppingListResource shoppingListResource, String username) {

        return new ShoppingList(UUID.randomUUID(), shoppingListResource.name(), username, Set.of());

    }
    


}
