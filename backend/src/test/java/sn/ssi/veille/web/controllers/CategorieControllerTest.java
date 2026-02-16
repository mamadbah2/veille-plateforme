package sn.ssi.veille.web.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import sn.ssi.veille.services.CategorieService;
import sn.ssi.veille.web.controllers.implementation.CategorieControllerImpl;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

@WebMvcTest(CategorieControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class CategorieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategorieService categorieService;

    @MockitoBean
    private sn.ssi.veille.models.repositories.CategorieRepository categorieRepository;

    @Test
    @DisplayName("Get All Categories - Returns List")
    void getAllCategories_ReturnsList() throws Exception {
        // Given
        CategorieResponse cat = new CategorieResponse("cat1", "Security", "Description", "#FF0000", "shield", null, 5L,
                java.time.LocalDateTime.now());
        given(categorieService.getAllCategories()).willReturn(List.of(cat));

        // When/Then
        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomCategorie").value("Security"));
    }
}
