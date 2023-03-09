package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.shoppinglist.ShoppingList;
import com.ezgroceries.shoppinglist.shoppinglist.services.ShoppingListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;


import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ShoppingListControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final UUID ID = UUID.randomUUID();

    private final String NAME = "Stephanie's birthday";

    private final Collection INGREDIENTS = List.of("Tequila",
            "Triple sec",
            "Lime juice",
            "Salt",
            "Blue Curacao");

    private final ShoppingList SHOPPINGLIST = new ShoppingList(NAME, ID,INGREDIENTS);


    @MockBean
    private ShoppingListService shoppingListService;

    @BeforeEach
    void Init(){

        when(shoppingListService.findAll()).thenReturn(List.of(SHOPPINGLIST));
        when(shoppingListService.findById(ID)).thenReturn(Optional.of(SHOPPINGLIST));
        when(shoppingListService.createShoppingList(any())).thenAnswer(args -> {
            //take shopping list to create and return it with a id.
            ShoppingList list = args.getArgument(0);
            list.setId(ID);
            return list;
        });

    }
    @Test
    void postShoppingList() throws Exception {

        ShoppingList stephanies = SHOPPINGLIST;
        stephanies.setId(null);

        mockMvc
                .perform(
                        post("/shopping-lists").contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(SHOPPINGLIST))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", containsString("/shopping-lists/"+ID)));

    }

    @Test
    void postShoppingListCocktail() throws Exception {


        mockMvc
                .perform(
                        post("/shopping-lists/"+ ID +"/cocktails").contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(SHOPPINGLIST))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", containsString("/shopping-lists/"+ ID)));

    }

    @Test
    void getShoppingListCocktail() throws Exception {


        mockMvc
                .perform(
                        get("/shopping-lists").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(ID.toString()))
                .andExpect(jsonPath("$.[*].name").value(NAME))
                .andExpect(jsonPath("$.[*].ingredients").value(hasItems(INGREDIENTS)));


    }
}
