package com.pokedex.pokedex.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pokedex.pokedex.dto.PokemonDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pokedex.pokedex.dto.PokemonInfoDTO;
import com.pokedex.pokedex.utils.PokemonTypeColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.List;

@SpringBootTest
public class InfoServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PokemonTypeColor pokemonTypeColor;

    @Mock
    private Gson gson;

    @InjectMocks
    private InfoService pokemonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(pokemonService, "BASE_URL", "https://pokeapi.co/api/v2/");
    }


    @Test
    void testGetPokemonInfoByIdOrName_Success() throws Exception {
        // Arrange
        String idOrName = "pikachu";
        String mockResponse = "{" +
                "\"id\": 25, \"name\": \"pikachu\", \"height\": 4, \"weight\": 60, " +
                "\"base_experience\": 112, \"types\": [{\"type\": {\"name\": \"electric\"}}], " +
                "\"abilities\": [{\"ability\": {\"name\": \"static\"}}]}";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(pokemonTypeColor.getTypeColor("electric")).thenReturn("yellow");
        when(pokemonTypeColor.getPokemonColorFromAPI(25)).thenReturn("yellow");

        // Act
        PokemonInfoDTO result = pokemonService.getPokemonInfoByIdOrName(idOrName);

        // Assert
        assertNotNull(result);
        assertEquals(25, result.getId());
        assertEquals("pikachu", result.getName());
        assertEquals(4, result.getHeight());
        assertEquals(60, result.getWeight());
        assertEquals(112, result.getBaseExperience());
        assertEquals("yellow", result.getColour());
        assertEquals(1, result.getTypes().size());
        assertEquals("electric", result.getTypes().get(0).getName());
        assertEquals("yellow", result.getTypes().get(0).getColor());
        assertEquals(1, result.getAbilities().size());
        assertEquals("static", result.getAbilities().get(0));

        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void testGetPokemonInfoByIdOrName_NullResponse() {
        // Arrange
        String idOrName = "invalid";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(null);

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            pokemonService.getPokemonInfoByIdOrName(idOrName);
        });

        // Assert
        assertTrue(exception.getMessage().contains("Rest Template giving Null Obj"));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void testGetPokemonInfoByIdOrName_InvalidJson() {
        // Arrange
        String idOrName = "invalid";
        String mockResponse = "{invalid_json}";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            pokemonService.getPokemonInfoByIdOrName(idOrName);
        });

        // Assert
        assertNotNull(exception);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }

    @Test
    void testGetPokemonList_Success() {
        // Arrange
        String limit = "limit=10";
        String mockResponse = "{" +
                "\"results\": [" +
                "{\"name\": \"bulbasaur\", \"url\": \"https://pokeapi.co/api/v2/pokemon/1/\"}," +
                "{\"name\": \"ivysaur\", \"url\": \"https://pokeapi.co/api/v2/pokemon/2/\"}" +
                "]}";

        JSONObject mockJsonObject = new JSONObject(mockResponse);
        JSONArray mockResultsArray = mockJsonObject.getJSONArray("results");
        Type listType = new TypeToken<List<PokemonDTO>>() {}.getType();
        List<PokemonDTO> mockPokemonList = List.of(
                new PokemonDTO(1, "bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/" ),
                new PokemonDTO(2, "ivysaur", "https://pokeapi.co/api/v2/pokemon/2/")
        );

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(gson.fromJson(anyString(), eq(listType))).thenReturn(mockPokemonList);

        // Act
        List<PokemonDTO> result = pokemonService.getPokemonList(limit);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("bulbasaur", result.get(0).getName());
        assertEquals(1, result.get(0).getId());
        assertEquals("ivysaur", result.get(1).getName());
        assertEquals(2, result.get(1).getId());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
        verify(gson, times(1)).fromJson(anyString(), eq(listType));
    }

    @Test
    void testGetPokemonList_EmptyResults() {
        // Arrange
        String limit = "limit=0";
        String mockResponse = "{" +
                "\"results\": []" +
                "}";

        JSONObject mockJsonObject = new JSONObject(mockResponse);
        JSONArray mockResultsArray = mockJsonObject.getJSONArray("results");
        Type listType = new TypeToken<List<PokemonDTO>>() {}.getType();
        List<PokemonDTO> mockPokemonList = List.of();

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);
        when(gson.fromJson(anyString(), eq(listType))).thenReturn(mockPokemonList);

        // Act
        List<PokemonDTO> result = pokemonService.getPokemonList(limit);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
        verify(gson, times(1)).fromJson(anyString(), eq(listType));
    }

    @Test
    void testGetPokemonList_InvalidResponse() {
        // Arrange
        String limit = "limit=10";
        String mockResponse = "invalid_json";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            pokemonService.getPokemonList(limit);
        });

        // Assert
        assertNotNull(exception);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
        verify(gson, never()).fromJson(anyString(), any(Type.class));
    }
}
