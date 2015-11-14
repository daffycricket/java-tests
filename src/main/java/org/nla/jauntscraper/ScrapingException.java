package org.nla.jauntscraper;

@SuppressWarnings("serial")
public class ScrapingException extends RuntimeException {

    public ScrapingException() {
        super();
    }

    public ScrapingException(String message) {
        super(message);
    }

    public ScrapingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScrapingException(String message, Throwable cause,
                             boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ScrapingException(Throwable cause) {
        super(cause);
    }

}
