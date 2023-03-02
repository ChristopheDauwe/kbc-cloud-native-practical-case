package com.ezgroceries.shoppinglist.groceries.cocktail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Cocktail {

    private UUID id;
    private String name;

    private String glass;

    private String instructions;
    private String image;
    private Collection<String> ingredients =  new ArrayList<>();
}
