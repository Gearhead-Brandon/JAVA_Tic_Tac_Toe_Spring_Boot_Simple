package game.tictactoe.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.tictactoe.application.TicTacToeApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TicTacToeApplication.class)
@AutoConfigureMockMvc
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateGameAndMakeFirstMove() throws Exception {
        RequestBuilder requestBuilder = post("/api/v1/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "playerSide": "O",
                            "gameField": [
                                [" ", " ", " "],
                                [" ", " ", " "],
                                [" ", " ", " "]
                            ]
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"))
                .andExpectAll(
                        jsonPath("gameField").value(hasSize(3)),
                        jsonPath("gameField").value(everyItem(hasSize(3))),
                        jsonPath("gameField").value(everyItem(everyItem(matchesPattern("[\\sOX]"))))
                );
    }

    @Test
    void testCreateGameAndMakeFirstMove_NotValidGameFieldForPlayerX() throws Exception {
        RequestBuilder requestBuilder = post("/api/v1/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "playerSide": "X",
                            "gameField": [
                                [" ", " ", " "],
                                [" ", " ", " "],
                                [" ", " ", " "]
                            ]
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "message":"With player X the field must have one X"
                                }
                                """)
                );
    }

    @Test
    void testCreateGameAndMakeFirstMove_NotValidGameFieldForPlayerO() throws Exception {
        RequestBuilder requestBuilder = post("/api/v1/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "playerSide": "O",
                            "gameField": [
                                ["O", " ", " "],
                                [" ", " ", " "],
                                [" ", " ", " "]
                            ]
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "message":"With player O the field must be empty"
                                }
                                """)
                );
    }

    @Test
    void testCreateGameAndMakeFirstMove_NotValidPlayerSideInBody() throws Exception {
        RequestBuilder requestBuilder = post("/api/v1/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "playerSide": "E",
                            "gameField": [
                                ["X", "X", "X"],
                                ["X", "X", "X"],
                                ["X", "X", "X"]
                            ]
                        }
                        """); // E - invalid player side, validator will throw exception

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "message":"Invalid player side, it should be 'X' or 'O'"
                                }
                                """)
                );
    }

    @Test
    void testUpdateGame() throws Exception {
        RequestBuilder requestBuilder = post("/api/v1/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "playerSide": "O",
                            "gameField": [
                                [" ", " ", " "],
                                [" ", " ", " "],
                                [" ", " ", " "]
                            ]
                        }
                        """);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"))
                .andExpectAll(
                        jsonPath("gameField").value(hasSize(3)),
                        jsonPath("gameField").value(everyItem(hasSize(3))),
                        jsonPath("gameField").value(everyItem(everyItem(matchesPattern("[\\sOX]"))))
                )
                .andReturn();

        String bodyGameField = result.getResponse().getContentAsString();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(bodyGameField);
            JsonNode gameFieldNode = rootNode.get("gameField");

            bodyGameField = "{ \"gameField\": " + gameFieldNode.toString()
                    .replaceFirst(" ", "O") + " }";

            System.out.println("AAAAA  + " + bodyGameField);

        } catch (IOException e) {
            e.printStackTrace();
        }

        UUID uuid = UUID.fromString(result.getResponse().getHeader("Location").replace("/api/v1/game/", ""));

        requestBuilder = put("/api/v1/game/" + uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyGameField);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("gameField").value(hasSize(3)),
                        jsonPath("gameField").value(everyItem(hasSize(3))),
                        jsonPath("gameField").value(everyItem(everyItem(matchesPattern("[\\sOX]"))))
                );
    }

    @Test
    void testUpdateGame_NotValidGameField() throws Exception {
        RequestBuilder requestBuilder = post("/api/v1/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "playerSide": "O",
                            "gameField": [
                                [" ", " ", " "],
                                [" ", " ", " "],
                                [" ", " ", " "]
                            ]
                        }
                        """);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"))
                .andExpectAll(
                        jsonPath("gameField").value(hasSize(3)),
                        jsonPath("gameField").value(everyItem(hasSize(3))),
                        jsonPath("gameField").value(everyItem(everyItem(matchesPattern("[\\sOX]"))))
                )
                .andReturn();

        UUID uuid = UUID.fromString(result.getResponse().getHeader("Location").replace("/api/v1/game/", ""));

        requestBuilder = put("/api/v1/game/" + uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""" 
                        {
                            "gameField": [
                                [" ", " ", " "],
                                [" ", " ", " "],
                                [" ", " ", " "]
                            ]
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "message":"Invalid game field"
                                }
                                """)
                );
    }

    @Test
    void testUpdateGame_NotExistGame() throws Exception {
        RequestBuilder requestBuilder = put("/api/v1/game/00000000-0000-0000-0000-00000000FFFF")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "gameField": [
                                ["O", " ", " "],
                                [" ", " ", " "],
                                [" ", " ", " "]
                            ]
                        }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isNotFound()
                );
    }
}
