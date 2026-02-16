package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sn.ssi.veille.exceptions.CategorieNotFoundException;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.repositories.CategorieRepository;
import sn.ssi.veille.models.repositories.StoryRepository;
import sn.ssi.veille.services.implementation.CategorieServiceImpl;
import sn.ssi.veille.web.dto.requests.CategorieRequest;
import sn.ssi.veille.web.dto.responses.CategorieResponse;

@ExtendWith(MockitoExtension.class)
class CategorieServiceImplTest {

    @Mock
    private CategorieRepository categorieRepository;
    @Mock
    private StoryRepository storyRepository;

    @InjectMocks
    private CategorieServiceImpl categorieService;

    private CategorieRequest categorieRequest;
    private Categorie categorie;

    @BeforeEach
    void setUp() {
        categorie = Categorie.builder()
                .id("cat1")
                .nomCategorie("Cybersecurity")
                .description("Security News")
                .couleur("#FF0000")
                .createdAt(LocalDateTime.now())
                .build();

        categorieRequest = new CategorieRequest("Cybersecurity", "Security News", "#FF0000", "shield");
    }

    @Test
    @DisplayName("Create Category - Valid - Returns Response")
    void createCategorie_Valid_ReturnsResponse() {
        // Given
        when(categorieRepository.save(any(Categorie.class))).thenReturn(categorie);
        when(storyRepository.countByCategoriesContaining(anyString())).thenReturn(5L);

        // When
        CategorieResponse response = categorieService.createCategorie(categorieRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.storiesCount()).isEqualTo(5);
        assertThat(response.nomCategorie()).isEqualTo("Cybersecurity");
    }

    @Test
    @DisplayName("Get Category By Id - Found - Returns Response")
    void getCategorieById_Found_ReturnsResponse() {
        // Given
        when(categorieRepository.findById("cat1")).thenReturn(Optional.of(categorie));
        when(storyRepository.countByCategoriesContaining("Cybersecurity")).thenReturn(10L);

        // When
        CategorieResponse response = categorieService.getCategorieById("cat1");

        // Then
        assertThat(response.storiesCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("Get Category By Id - Not Found - Throws Exception")
    void getCategorieById_NotFound_ThrowsException() {
        // Given
        when(categorieRepository.findById("unknown")).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> categorieService.getCategorieById("unknown"))
                .isInstanceOf(CategorieNotFoundException.class);
    }
}
