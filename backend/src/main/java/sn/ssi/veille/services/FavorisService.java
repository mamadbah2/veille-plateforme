package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.FavorisRequest;
import sn.ssi.veille.web.dto.requests.UpdateFavorisRequest;
import sn.ssi.veille.web.dto.responses.FavorisResponse;
import sn.ssi.veille.web.dto.responses.PageResponse;

public interface FavorisService {

    FavorisResponse addToFavoris(FavorisRequest request);

    PageResponse<FavorisResponse> getCurrentUserFavoris(int page, int size);

    FavorisResponse getFavorisById(String favorisId);

    FavorisResponse updateFavoris(String favorisId, UpdateFavorisRequest request);

    void removeFavoris(String favorisId);

    void removeFromFavorisByArticle(String articleId);

    boolean isArticleInFavoris(String articleId);

    long getFavorisCount();

    PageResponse<FavorisResponse> searchFavorisByTag(String tag, int page, int size);
}
