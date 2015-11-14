package org.nla.model;

import java.time.LocalDateTime;
import java.util.List;

public class Book {

    private List<String> authors;
    private String image;
    private String isbn;
    private Integer numberOfPages;
    private String publication;
    private LocalDateTime releaseDate;
    private String title;
    private Integer yearOfPublication;

    public Book(String isbn, String title, List<String> authors,
                String publication, Integer yearOfPublication,
                LocalDateTime releaseDate, Integer numberOfPages, String image) {

        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.publication = publication;
        this.yearOfPublication = yearOfPublication;
        this.numberOfPages = numberOfPages;
        this.image = image;
        this.releaseDate = releaseDate;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getImage() {
        return image;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public String getPublication() {
        return publication;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }
}