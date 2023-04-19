package com.ezgroceries.shoppinglist.shoppinglist;

import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import com.ezgroceries.shoppinglist.shoppinglist.domain.ShoppingList;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Mapper(componentModel = "spring")
public interface ShoppingListMapper {

    default ShoppingListResource toShoppingListResource(ShoppingList shoppingList, Collection<String> ingredients) {

        return new ShoppingListResource(shoppingList.getId(),shoppingList.getName(),ingredients);

    }
    


}
