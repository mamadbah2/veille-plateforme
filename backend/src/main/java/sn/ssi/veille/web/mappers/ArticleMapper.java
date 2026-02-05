package sn.ssi.veille.web.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.models.entities.Categorie;
import sn.ssi.veille.models.entities.Source;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.responses.ArticleResponse;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;

import java.util.List;

/**
 * Mapper MapStruct pour la conversion entre Article entity et DTOs.
 * 
 * <p>Ce mapper nécessite des objets Source et Categorie en contexte
 * pour construire les réponses complètes.</p>
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@Mapper(componentModel = "spring", 
        uses = {SourceMapper.class, CategorieMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ArticleMapper {

    /**
     * Convertit un Article entity vers un ArticleResponse DTO complet.
     * 
     * @param article l'entité Article
     * @param source la source associée
     * @param categorie la catégorie associée
     * @return le DTO ArticleResponse
     */
    // @Mapping(target = "source", source = "source")
    // @Mapping(target = "categorie", source = "categorie")
    // ArticleResponse toResponse(Article article, Source source, Categorie categorie);

    /**
     * Convertit un Article entity vers un ArticleSummaryResponse DTO.
     *
     * @param article l'entité Article
     * @param nomSource le nom de la source
     * @param nomCategorie le nom de la catégorie
     * @param couleurCategorie la couleur de la catégorie
     * @return le DTO ArticleSummaryResponse
     */
    @Mapping(target = "nomSource", source = "nomSource")
    @Mapping(target = "nomCategorie", source = "nomCategorie")
    @Mapping(target = "couleurCategorie", source = "couleurCategorie")
    ArticleSummaryResponse toSummaryResponse(Article article, String nomSource, String nomCategorie, String couleurCategorie);

    /**
     * Convertit une liste d'Article entities vers une liste d'ArticleSummaryResponse DTOs.
     * Note: Cette méthode nécessite un contexte avec les sources et catégories.
     *
     * @param articles la liste des entités Article
     * @return la liste des DTOs ArticleSummaryResponse
     */
    List<ArticleSummaryResponse> toSummaryResponseList(List<Article> articles);

    /**
     * Convertit une requête de création vers une entité Article.
     *
     * @param request la requête de création
     * @return l'entité Article
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nombreVues", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Article toEntity(ArticleRequest request);

    /**
     * Met à jour une entité Article existante avec les données de la requête.
     *
     * @param request la requête de mise à jour
     * @param article l'entité Article à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nombreVues", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ArticleRequest request, @MappingTarget Article article);
}
