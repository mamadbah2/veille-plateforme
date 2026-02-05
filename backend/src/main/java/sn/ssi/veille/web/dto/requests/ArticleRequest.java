package sn.ssi.veille.web.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import sn.ssi.veille.models.entities.Gravite;

import java.time.LocalDateTime;

/**
 * DTO pour la création/modification d'un article.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Schema(description = "Données pour créer ou modifier un article")
// public record ArticleRequest(

//         @Schema(description = "Titre de l'article", example = "Nouvelle vulnérabilité critique dans Log4j")
//         @NotBlank(message = "Le titre est obligatoire")
//         @Size(min = 5, max = 300, message = "Le titre doit contenir entre 5 et 300 caractères")
//         String titre,

//         @Schema(description = "Contenu complet de l'article")
//         @NotBlank(message = "Le contenu est obligatoire")
//         String contenu,

//         @Schema(description = "Résumé de l'article")
//         @Size(max = 1000, message = "Le résumé ne peut pas dépasser 1000 caractères")
//         String resume,

//         @Schema(description = "URL originale de l'article", example = "https://example.com/article/123")
//         @NotBlank(message = "L'URL d'origine est obligatoire")
//         String urlOrigine,

//         @Schema(description = "URL de l'image de couverture")
//         String imageUrl,

//         @Schema(description = "ID de la source", example = "65f1a2b3c4d5e6f7g8h9i0j1")
//         @NotNull(message = "L'ID de la source est obligatoire")
//         String sourceId,

//         @Schema(description = "ID de la catégorie", example = "65f1a2b3c4d5e6f7g8h9i0j2")
//         @NotNull(message = "L'ID de la catégorie est obligatoire")
//         String categorieId,

//         @Schema(description = "Niveau de gravité", example = "IMPORTANT")
//         Gravite gravite,

//         @Schema(description = "Tags associés à l'article", example = "[\"log4j\", \"vulnerability\", \"java\"]")
//         String[] tags,

//         @Schema(description = "Auteur de l'article", example = "John Doe")
//         String auteur,

//         @Schema(description = "Date de publication originale")
//         LocalDateTime datePublication

// ) {}
@Getter
@Setter
public class ArticleRequest {

     @Schema(description = "Titre de l'article", example = "Nouvelle vulnérabilité critique dans Log4j")
        @NotBlank(message = "Le titre est obligatoire")
        @Size(min = 5, max = 300, message = "Le titre doit contenir entre 5 et 300 caractères")
        private String titre;

        @Schema(description = "Contenu complet de l'article")
        @NotBlank(message = "Le contenu est obligatoire")
        private String contenu;
        @Schema(description = "Résumé de l'article")
        @Size(max = 1000, message = "Le résumé ne peut pas dépasser 1000 caractères")
        private String resume;
}