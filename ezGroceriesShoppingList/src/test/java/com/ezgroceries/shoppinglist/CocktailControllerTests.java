package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBResponse;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.DrinkResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CocktailControllerTests {

    @Autowired
    private MockMvc mockMvc;
    private final String STRID = "1102";
    private final UUID ID = UUID.nameUUIDFromBytes(STRID.getBytes());
    private final String NAME = "Margerita";
    private final String INGREDIENT1 = "Tequila";
    private final String INGREDIENT2 = "Triple sec";
    private final String INGREDIENT3 = "Lime juice";
    private final Collection INGREDIENTS = List.of(INGREDIENT1,INGREDIENT2,INGREDIENT3);
    private final String GLASS = "Cocktail glass";
    private final String INSTRUCTIONS = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..";
    private final String IMAGE = "https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg";
    private final CocktailDBResponse cocktailDBResponse = new CocktailDBResponse(List.of(new DrinkResource(STRID,NAME,GLASS,INSTRUCTIONS,IMAGE,INGREDIENT1,INGREDIENT2,INGREDIENT3)));
    @MockBean
    private CocktailDBClient cocktailClient;


    @BeforeEach
    void Init(){
        when(cocktailClient.searchCocktails(any())).thenReturn(cocktailDBResponse);
    }


    @Test
    void getCocktail() throws Exception {


        mockMvc
                .perform(
                        get("/cocktails?search=s").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(ID.toString()))
                .andExpect(jsonPath("$.[*].name").value(NAME))
                .andExpect(jsonPath("$.[*].glass").value(GLASS))
                .andExpect(jsonPath("$.[*].instructions").value(INSTRUCTIONS))
                .andExpect(jsonPath("$.[*].image").value(IMAGE))
                .andExpect(jsonPath("$.[*].ingredients").value(hasItems(INGREDIENTS)));


    }
}
