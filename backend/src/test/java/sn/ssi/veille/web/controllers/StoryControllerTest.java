package sn.ssi.veille.web.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.models.entities.Story;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.models.repositories.SourceRepository;
import sn.ssi.veille.models.repositories.StoryRepository;
import sn.ssi.veille.services.StoryService;
import sn.ssi.veille.web.controllers.implementation.StoryControllerImpl;

@WebMvcTest(StoryControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Security
class StoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StoryService storyService;

    @MockitoBean
    private CategorieRepository categorieRepository;

    @MockitoBean
    private SourceRepository sourceRepository;

    @MockitoBean
    private StoryRepository storyRepository;

    @Test
    @DisplayName("Get Trending Stories - Valid -> Returns 200 OK & List")
    void getTrendingStories_ReturnsList() throws Exception {
        // Given
        Story story = new Story();
        story.setId("story1");
        story.setTitre("Critical Vulnerability");

        Article article = new Article();
        article.setId("art1");
        article.setTitre("Understanding CVE-2026");
        article.setContenu("Detailed analysis...");
        article.setSourceId("src1");
        story.setArticles(List.of(article));

        Source source = new Source();
        source.setId("src1");
        source.setNomSource("NIST");

        given(storyService.getTrendingStories()).willReturn(List.of(story));
        given(sourceRepository.findById("src1")).willReturn(Optional.of(source));

        // When/Then
        mockMvc.perform(get("/api/v1/stories/trending")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("story1"))
                .andExpect(jsonPath("$[0].titre").value("Critical Vulnerability"))
                .andExpect(jsonPath("$[0].mainSourceName").value("NIST"));
    }

    @Test
    @DisplayName("Get Story By ID - Exists -> Returns 200 OK")
    void getStoryById_Exists_ReturnsStory() throws Exception {
        // Given
        Story story = new Story();
        story.setId("story1");
        story.setTitre("Ransomware Attack");
        story.setArticles(Collections.emptyList());

        given(storyService.getStoryById("story1")).willReturn(story);

        // When/Then
        mockMvc.perform(get("/api/v1/stories/{id}", "story1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("story1"));
    }

    @Test
    @DisplayName("Get Story By ID - Not Found -> Returns 404")
    void getStoryById_NotFound_Returns404() throws Exception {
        // Given
        given(storyService.getStoryById("unknown")).willReturn(null);

        // When/Then
        mockMvc.perform(get("/api/v1/stories/{id}", "unknown")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get Stories By Category - Valid -> Returns 200 OK")
    void getStoriesByCategory_ReturnsList() throws Exception {
        // Given
        Story story = new Story();
        story.setId("story2");
        story.setTitre("AI Regulations");
        story.setArticles(Collections.emptyList());

        given(storyRepository.findByCategoriesContaining("Legal")).willReturn(List.of(story));

        // When/Then
        mockMvc.perform(get("/api/v1/stories/category/{categoryName}", "Legal")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("story2"));
    }
}
