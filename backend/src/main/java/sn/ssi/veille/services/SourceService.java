package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

public interface SourceService {

    SourceResponse createSource(SourceRequest request);

    SourceResponse getSourceById(String id);

    List<SourceResponse> getAllSources();

    List<SourceResponse> getActiveSources();

    SourceResponse updateSource(String id, SourceRequest request);

    SourceResponse activateSource(String id);

    SourceResponse deactivateSource(String id);

    void deleteSource(String id);

}
