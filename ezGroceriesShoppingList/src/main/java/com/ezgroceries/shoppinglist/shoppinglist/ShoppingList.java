package com.ezgroceries.shoppinglist.shoppinglist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class ShoppingList {

    private String name;

    private UUID id;

    private Collection<String> ingredients = new ArrayList<>();
}
