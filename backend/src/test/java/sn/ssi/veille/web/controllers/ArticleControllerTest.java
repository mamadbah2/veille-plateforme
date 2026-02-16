package sn.ssi.veille.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import sn.ssi.veille.models.entities.Gravite;
import sn.ssi.veille.services.AIService;
import sn.ssi.veille.services.ContentExtractionService;
import sn.ssi.veille.services.ScrapingService;
import sn.ssi.veille.services.implementation.ArticleServiceImpl;
import sn.ssi.veille.web.controllers.implementation.ArticleControllerImpl;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.requests.ArticleSearchCriteria;
import sn.ssi.veille.web.dto.responses.ArticleResponse;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

@WebMvcTest(ArticleControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Security Filters
class ArticleControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ArticleServiceImpl articleService;

        @MockitoBean
        private AIService aiService;

        @MockitoBean
        private ContentExtractionService contentExtractionService;

        @MockitoBean
        private ScrapingService scrapingService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        @DisplayName("Get Latest Articles - Valid Request -> Returns 200 OK & Page")
        void getLatestArticles_ReturnsPage() throws Exception {
                // Given
                ArticleSummaryResponse summary = new ArticleSummaryResponse(
                                "1", "Title", "Summary", "http://url.com", Gravite.INFORMATION,
                                "source1", "cat1", "#000000", 100L, LocalDateTime.now());
                Page<ArticleSummaryResponse> page = new PageImpl<>(List.of(summary));
                PageResponse<ArticleSummaryResponse> response = new PageResponse<>(
                                List.of(summary), 0, 1, 1, 1, true, true);

                given(articleService.getLatestArticles(anyInt(), anyInt())).willReturn(response);

                // When/Then
                mockMvc.perform(get("/api/v1/articles")
                                .param("page", "0")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value("1"))
                                .andExpect(jsonPath("$.content[0].titre").value("Title"));
        }

        @Test
        @DisplayName("Get Article By ID - Exists -> Returns 200 OK")
        void getArticleById_Exists_ReturnsArticle() throws Exception {
                // Given
                ArticleResponse response = new ArticleResponse(
                                "1", "Title", "Content", "Summary", "http://url.com", "http://img.url",
                                Gravite.INFORMATION, new String[] { "tag" }, "Author", 100L, LocalDateTime.now(),
                                null, null, LocalDateTime.now());

                given(articleService.getArticleById("1")).willReturn(response);

                // When/Then
                mockMvc.perform(get("/api/v1/articles/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value("1"))
                                .andExpect(jsonPath("$.titre").value("Title"));
        }

        @Test
        @DisplayName("Create Article - Valid Input -> Returns 201 Created")
        void createArticle_ValidInput_ReturnsCreated() throws Exception {
                // Given
                ArticleRequest request = new ArticleRequest(
                                "Title", "Content", "Summary", "http://url.com", "http://img.url",
                                "source1", "cat1", Gravite.INFORMATION, new String[] { "tag" }, "Author",
                                LocalDateTime.now());

                ArticleResponse response = new ArticleResponse(
                                "1", "Title", "Content", "Summary", "http://url.com", "http://img.url",
                                Gravite.INFORMATION, new String[] { "tag" }, "Author", 0L, LocalDateTime.now(),
                                null, null, LocalDateTime.now());

                given(articleService.createArticle(any(ArticleRequest.class))).willReturn(response);

                // When/Then
                mockMvc.perform(post("/api/v1/articles")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value("1"));
        }

        @Test
        @DisplayName("Search Articles - Valid Criteria -> Returns Page")
        void searchArticles_ValidCriteria_ReturnsPage() throws Exception {
                // Given
                ArticleSearchCriteria criteria = new ArticleSearchCriteria(
                                "keyword", null, null, null, null, null, null, null, 0, 10, null, null);

                ArticleSummaryResponse summary = new ArticleSummaryResponse(
                                "1", "Title", "Summary", "http://url.com", Gravite.INFORMATION,
                                "source1", "cat1", "#000000", 100L, LocalDateTime.now());
                PageResponse<ArticleSummaryResponse> response = new PageResponse<>(
                                List.of(summary), 0, 1, 1, 1, true, true);

                given(articleService.searchArticles(any(ArticleSearchCriteria.class))).willReturn(response);

                // When/Then
                mockMvc.perform(post("/api/v1/articles/search")
                                .content(objectMapper.writeValueAsString(criteria))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value("1"));
        }
}
