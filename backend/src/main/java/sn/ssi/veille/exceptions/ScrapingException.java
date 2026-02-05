package sn.ssi.veille.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lors d'une erreur de scraping.
 *
 * @author Équipe Backend SSI
 * @version 1.0
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ScrapingException extends RuntimeException {

    private final String sourceId;

    public ScrapingException(String message) {
        super(message);
        this.sourceId = null;
    }

    public ScrapingException(String message, String sourceId) {
        super(message);
        this.sourceId = sourceId;
    }

    public ScrapingException(String message, Throwable cause) {
        super(message, cause);
        this.sourceId = null;
    }

    public ScrapingException(String message, String sourceId, Throwable cause) {
        super(message, cause);
        this.sourceId = sourceId;
    }

    public String getSourceId() {
        return sourceId;
    }
}
