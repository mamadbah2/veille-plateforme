package sn.ssi.veille.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entité représentant une catégorie d'articles de veille.
 * Permet de classifier les articles par thématique.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Categorie {

    /**
     * Identifiant unique de la catégorie.
     */
    @Id
    private String id;

    /**
     * Nom unique de la catégorie.
     */
    @Indexed(unique = true)
    private String nomCategorie;

    /**
     * Description de la catégorie.
     */
    private String description;

    /**
     * Couleur associée (pour l'affichage front).
     */
    private String couleur;

    /**
     * Icône associée (nom de l'icône).
     */
    private String icone;

    /**
     * Date de création.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
