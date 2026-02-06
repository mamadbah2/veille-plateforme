package sn.ssi.veille.services;

import sn.ssi.veille.web.dto.requests.SourceRequest;
import sn.ssi.veille.web.dto.responses.SourceResponse;

import java.util.List;

public interface SourceService {

    SourceResponse createSource(SourceRequest request);

    SourceResponse getSourceById(String id);

    List<SourceResponse> getAllSources();

}
