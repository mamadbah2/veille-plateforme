package sn.ssi.veille.web.controllers;

import org.springframework.http.ResponseEntity;
import sn.ssi.veille.web.dto.responses.StoryResponse;

import java.util.List;

public interface StoryController {

    ResponseEntity<List<StoryResponse>> getTrendingStories();

    ResponseEntity<StoryResponse> getStoryById(String id);
}
