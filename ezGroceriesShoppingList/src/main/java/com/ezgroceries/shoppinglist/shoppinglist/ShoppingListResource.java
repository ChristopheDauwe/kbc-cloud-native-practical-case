package com.ezgroceries.shoppinglist.shoppinglist;

import java.util.Collection;
import java.util.UUID;


public record ShoppingListResource(UUID id, String name, Collection<String> ingredients) {}
