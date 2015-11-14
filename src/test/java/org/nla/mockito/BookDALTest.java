package org.nla.mockito;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.nla.model.Book;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookDALTest {

    private static Book book1;

    private static Book book2;

    @Mock
    private static BookDAL toTest;

    @BeforeClass
    public static void setUp() throws Exception {
        toTest = Mockito.mock(BookDAL.class);

        book1 = new Book("8131721019", "Compilers Principles", Arrays.asList(
                "D. Jeffrey Ulman", "Ravi Sethi", "Alfred V. Aho",
                "Monica S. Lam"), "Pearson Education Singapore Pte Ltd", 2008,
                LocalDateTime.now(), 1009, "BOOK_IMAGE");

        book2 = new Book("9788183331630", "Let Us C 13th Edition",
                Arrays.asList("Yashavant Kanetkar"), "BPB PUBLICATIONS", 2012,
                LocalDateTime.now(), 675, "BOOK_IMAGE");

        Mockito.when(toTest.getAllBooks()).thenReturn(
                Arrays.asList(book1, book2));
        Mockito.when(toTest.getBook("8131721019")).thenReturn(book1);
        Mockito.when(toTest.addBook(book1)).thenReturn(book1.getIsbn());
        Mockito.when(toTest.updateBook(book1)).thenReturn(book1.getIsbn());
    }

    @Test
    public void testAddBook() throws Exception {
        String isbn = toTest.addBook(book1);
        assertThat(isbn).isNotNull();
        assertThat(book1.getIsbn()).isEqualTo(isbn);
    }

    @Test
    public void testGetAllBooks() throws Exception {
        List<Book> allBooks = toTest.getAllBooks();
        assertThat(allBooks.size()).isEqualTo(2);
        Book myBook = allBooks.get(0);
        assertThat("8131721019").isEqualTo(myBook.getIsbn());
        assertThat("Compilers Principles").isEqualTo(myBook.getTitle());
        assertThat(4).isEqualTo(myBook.getAuthors().size());
        assertThat(2008).isEqualTo(myBook.getYearOfPublication());
        assertThat(1009).isEqualTo(myBook.getNumberOfPages());
        assertThat("Pearson Education Singapore Pte Ltd").isEqualTo(myBook.getPublication());
        assertThat("BOOK_IMAGE").isEqualTo(myBook.getImage());
    }

    @Test
    public void testGetBook() throws Exception {
        String isbn = "8131721019";

        Book myBook = toTest.getBook(isbn);

        assertThat(myBook).isNotNull();
        assertThat(isbn).isEqualTo(myBook.getIsbn());
        assertThat("Compilers Principles").isEqualTo(myBook.getTitle());
        assertThat(4).isEqualTo(myBook.getAuthors().size());
        assertThat("Pearson Education Singapore Pte Ltd").isEqualTo(myBook.getPublication());
        assertThat(2008).isEqualTo(myBook.getYearOfPublication());
        assertThat(1009).isEqualTo(myBook.getNumberOfPages());
    }

    @Test
    public void testUpdateBook() throws Exception {
        String isbn = toTest.updateBook(book1);
        assertThat(isbn).isNotNull();
        assertThat(book1.getIsbn()).isEqualTo(isbn);
    }
}
