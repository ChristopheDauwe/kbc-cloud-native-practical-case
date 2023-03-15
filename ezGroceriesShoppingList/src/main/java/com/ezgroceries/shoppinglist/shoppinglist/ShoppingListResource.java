package com.ezgroceries.shoppinglist.shoppinglist;

import java.util.Collection;
import java.util.UUID;

/**
 *
 * @param id
 * @param name
 * @param ingredients
 */
public record ShoppingListResource(UUID id, String name, Collection<String> ingredients) {}
