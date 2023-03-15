package com.ezgroceries.shoppinglist.groceries.cocktail.domain;

import com.ezgroceries.shoppinglist.utils.StringSetConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "cocktail")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Cocktail {

    @Id
    @Column(name = "id", unique = true)
    private UUID id ;

    @NotNull
    @Column(name = "id_drink", nullable = false)
    private String idDrink;
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ingredients")
    @Convert(converter = StringSetConverter.class)
    private Collection<String> ingredients;



}
