package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBResponse;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.DrinkResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import com.ezgroceries.shoppinglist.groceries.cocktail.services.CocktailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.ezgroceries.shoppinglist.TestUtil.UUID_REGEX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("hsqldb")
class CocktailResourceControllerTests {

    @Autowired
    private MockMvc mockMvc;
    private static final String DRINKID = "1102";
    private static final UUID ID = UUID.nameUUIDFromBytes(DRINKID.getBytes());
    private static final String NAME = "Margerita";
    private static final String INGREDIENT1 = "Tequila";
    private static final String INGREDIENT2 = "Triple sec";
    private static final String INGREDIENT3 = "Lime juice";
    private static final Collection<String> INGREDIENTS = List.of(INGREDIENT1,INGREDIENT2,INGREDIENT3);
    private static final String GLASS = "CocktailDTO glass";
    private static final String INSTRUCTIONS = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..";
    private static final String IMAGEURL = "https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg";
    private static final CocktailDBResponse cocktailDBResponse = new CocktailDBResponse(List.of(new DrinkResource(DRINKID,NAME,GLASS,INSTRUCTIONS, IMAGEURL,INGREDIENT1,INGREDIENT2,INGREDIENT3)));


    public static final Cocktail COCKTAIL = new Cocktail(ID,DRINKID,NAME,GLASS,INSTRUCTIONS, IMAGEURL,INGREDIENTS);

    @MockBean
    private CocktailDBClient cocktailClient;

    @Autowired
    private CocktailService cocktailService;


    @BeforeEach
    void Init(){
        when(cocktailClient.searchCocktails(any())).thenReturn(cocktailDBResponse);
    }


    @Test
    @Transactional
    void getCocktail() throws Exception {

        int countBeforeSave = cocktailService.findAllInDB().size();

        mockMvc
                .perform(
                        get("/cocktails?search=s").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]",hasSize(1)))
                .andExpect(jsonPath("$.[0].id",matchesPattern(UUID_REGEX)))
                .andExpect(jsonPath("$.[0].name",hasToString(NAME)))
                .andExpect(jsonPath("$.[0].glass",hasToString(GLASS)))
                .andExpect(jsonPath("$.[0].instructions",hasToString(INSTRUCTIONS)))
                .andExpect(jsonPath("$.[0].image",hasToString(IMAGEURL)))
                .andExpect(jsonPath("$.[0].ingredients",hasSize(3)))
                .andExpect(jsonPath("$.[0].ingredients",containsInAnyOrder(INGREDIENTS.toArray())));


        int countAfterSave = cocktailService.findAllInDB().size();
        assertThat(countBeforeSave+1,is(countAfterSave));

        Collection<Cocktail> cocktails = cocktailService.findAllInDB();
        Cocktail cocktail = cocktails.stream().skip(countBeforeSave).findFirst().get();

        assertThat(cocktail.getIdDrink(), is(DRINKID));
        assertThat(cocktail.getName(), is(NAME));
        assertThat(cocktail.getGlass(), is(GLASS));
        assertThat(cocktail.getImageURL(), is(IMAGEURL));
        assertThat(cocktail.getInstructions(), is(INSTRUCTIONS));
        assertThat(cocktail.getId().toString(), matchesPattern(UUID_REGEX));
        assertThat(cocktail.getIngredients(), containsInAnyOrder(INGREDIENTS.toArray()));

    }
}
