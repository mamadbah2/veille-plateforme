package sn.ssi.veille.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sn.ssi.veille.exceptions.SourceAlreadyExistsException;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.models.repositories.SourceRepository;
import sn.ssi.veille.services.implementation.SourceServiceImpl;
import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.SourceResponse;
import sn.ssi.veille.models.entities.MethodeCollecte;

@ExtendWith(MockitoExtension.class)
class SourceServiceImplTest {

    @Mock
    private SourceRepository sourceRepository;

    @InjectMocks
    private SourceServiceImpl sourceService;

    private SourceRequest sourceRequest;

    @BeforeEach
    void setUp() {
        sourceRequest = new SourceRequest(
                "http://example.com/rss",
                "Example Source",
                "Description",
                "http://logo.com",
                MethodeCollecte.RSS,
                true,
                60,
                null,
                null,
                "fr",
                5,
                "cat1",
                30,
                3,
                60,
                100,
                5,
                true,
                Source.SourceType.MEDIA,
                null, null, null);
    }

    @Test
    @DisplayName("Create Source - Valid Request -> Return Response")
    void createSource_Valid_ReturnsResponse() {
        // Given
        when(sourceRepository.findByUrl(anyString())).thenReturn(Optional.empty());
        when(sourceRepository.save(any(Source.class))).thenAnswer(invocation -> {
            Source s = invocation.getArgument(0);
            s.setId("source1");
            return s;
        });

        // When
        SourceResponse response = sourceService.createSource(sourceRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo("source1");
        assertThat(response.nomSource()).isEqualTo("Example Source");
    }

    @Test
    @DisplayName("Create Source - Duplicate URL -> Throw Exception")
    void createSource_DuplicateUrl_ThrowsException() {
        // Given
        when(sourceRepository.findByUrl(anyString())).thenReturn(Optional.of(new Source()));

        // When/Then
        assertThatThrownBy(() -> sourceService.createSource(sourceRequest))
                .isInstanceOf(SourceAlreadyExistsException.class);
    }

    @Test
    @DisplayName("Update Source - Partial Update -> Should Update Fields")
    void updateSource_PartialUpdate_UpdatesFields() {
        // Given
        Source existing = Source.builder()
                .id("source1")
                .nomSource("Old Name")
                .active(false)
                .build();
        when(sourceRepository.findById("source1")).thenReturn(Optional.of(existing));
        when(sourceRepository.save(any(Source.class))).thenReturn(existing);

        SourceRequest updateRequest = new SourceRequest(
                null, "New Name", null, null, null, true, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null);

        // When
        SourceResponse response = sourceService.updateSource("source1", updateRequest);

        // Then
        assertThat(response.nomSource()).isEqualTo("New Name");
        assertThat(response.active()).isTrue(); // Updated to true
        verify(sourceRepository).save(existing);
    }
}
