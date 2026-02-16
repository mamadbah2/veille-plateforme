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

import sn.ssi.veille.services.SourceService;
import sn.ssi.veille.web.controllers.implementation.SourceControllerImpl;
import sn.ssi.veille.web.dto.responses.SourceResponse;

@WebMvcTest(SourceControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
class SourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SourceService sourceService;

    @MockitoBean
    private sn.ssi.veille.models.repositories.SourceRepository sourceRepository;

    @MockitoBean
    private sn.ssi.veille.models.repositories.CategorieRepository categorieRepository;

    @MockitoBean
    private sn.ssi.veille.services.ScrapingService scrapingService;

    @Test
    @DisplayName("Get All Sources - Returns List")
    void getAllSources_ReturnsList() throws Exception {
        // Given
        SourceResponse src = new SourceResponse(
                "src1", "http://nist.gov", "NIST", "National Vulnerability Database", "http://logo.url",
                sn.ssi.veille.models.entities.MethodeCollecte.API, true, 60, null, null,
                "en", 10, "Security", 10, true, sn.ssi.veille.models.entities.Source.SourceType.OFFICIAL,
                100L, 10, null, null, 0, java.time.LocalDateTime.now());
        given(sourceService.getAllSources()).willReturn(List.of(src));

        // When/Then
        mockMvc.perform(get("/api/v1/sources")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomSource").value("NIST"));
    }
}
