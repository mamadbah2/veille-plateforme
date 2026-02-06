package sn.ssi.veille.web.mappers;

import org.mapstruct.MappingTarget;
import sn.ssi.veille.models.entities.Article;
import sn.ssi.veille.web.dto.requests.ArticleRequest;
import sn.ssi.veille.web.dto.responses.ArticleSummaryResponse;

import java.util.List;

public interface ArticleMapper {

    // ArticleResponse toResponse(Article article, Source source, Categorie categorie);

    ArticleSummaryResponse toSummaryResponse(Article article, String nomSource, String nomCategorie, String couleurCategorie);

    List<ArticleSummaryResponse> toSummaryResponseList(List<Article> articles);

    Article toEntity(ArticleRequest request);

    void updateEntity(ArticleRequest request, @MappingTarget Article article);
}
