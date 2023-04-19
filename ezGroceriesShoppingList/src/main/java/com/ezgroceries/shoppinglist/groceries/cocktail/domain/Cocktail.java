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

    @Column(name = "glass")
    private String glass;

    @Column(name = "instructions")
    private String instructions;


    @Column(name = "image_url")
    private String imageURL;


    @Column(name = "ingredients")
    @Convert(converter = StringSetConverter.class)
    private Collection<String> ingredients;


    /**
     *
     * @param id
     * @param idDrink
     * @param name
     * @param glass
     * @param instructions
     * @param imageURL
     * @param ingredients
     */
    public Cocktail(UUID id, String idDrink, String name, String glass, String instructions, String imageURL, Collection<String> ingredients) {
        this.id = id;
        this.idDrink = idDrink;
        this.name = name;
        this.glass = glass;
        this.instructions = instructions;
        this.imageURL = imageURL;
        this.ingredients = ingredients;
    }
}
