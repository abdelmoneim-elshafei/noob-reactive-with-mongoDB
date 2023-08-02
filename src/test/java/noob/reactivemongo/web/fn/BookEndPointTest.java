package noob.reactivemongo.web.fn;

import noob.reactivemongo.model.BookDTO;
import noob.reactivemongo.service.Impl.BookServiceImplTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BookEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testPatchIdNotFound() {
        webTestClient.patch()
                .uri(BookRouterConfig.BOOK_PATH_ID, 999)
                .body(Mono.just(BookServiceImplTest.getTestBook()), BookDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testPatchIdFound() {
        BookDTO bookDTO = getSavedTestBook();

        webTestClient.patch()
                .uri(BookRouterConfig.BOOK_PATH_ID, bookDTO.getBookId())
                .body(Mono.just(bookDTO), BookDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteNotFound() {
        webTestClient.delete()
                .uri(BookRouterConfig.BOOK_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteBook() {
        BookDTO bookDTO = getSavedTestBook();

        webTestClient.delete()
                .uri(BookRouterConfig.BOOK_PATH_ID, bookDTO.getBookId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(4)
    void testUpdateBookBadRequest() {
        BookDTO testBook = getSavedTestBook();
        testBook.setBookName("");

        webTestClient.put()
                .uri(BookRouterConfig.BOOK_PATH_ID, testBook)
                .body(Mono.just(testBook), BookDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateBookNotFound() {
        webTestClient.put()
                .uri(BookRouterConfig.BOOK_PATH_ID, 999)
                .body(Mono.just(BookServiceImplTest.getTestBook()), BookDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void testUpdateBook() {

        BookDTO bookDTO = getSavedTestBook();
        bookDTO.setBookName("New");

        webTestClient.put()
                .uri(BookRouterConfig.BOOK_PATH_ID, bookDTO.getBookId())
                .body(Mono.just(bookDTO), BookDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testCreateBookBadData() {
        BookDTO testBook = BookServiceImplTest.getTestBook();
        testBook.setBookName("");

        webTestClient.post().uri(BookRouterConfig.BOOK_PATH)
                .body(Mono.just(testBook), BookDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateBook() {
        BookDTO testDto = getSavedTestBook();

        webTestClient.post().uri(BookRouterConfig.BOOK_PATH)
                .body(Mono.just(testDto), BookDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("location");
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(BookRouterConfig.BOOK_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(1)
    void testGetById() {
        BookDTO bookDTO = getSavedTestBook();

        webTestClient.get().uri(BookRouterConfig.BOOK_PATH_ID, bookDTO.getBookId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BookDTO.class);
    }

    @Test
    @Order(2)
    void testListBooks() {
        webTestClient.get().uri(BookRouterConfig.BOOK_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }

    @Test
    void testGetBookByIsbn() {
        final String isbn= "343-4343434-988";
        BookDTO bookDTO = getSavedTestBook();
        bookDTO.setBookIsbn(isbn);

        webTestClient.post().uri(BookRouterConfig.BOOK_PATH)
               .body(Mono.just(bookDTO),BookDTO.class)
               .header("Content-type","application/json")
               .exchange();

        webTestClient.get().uri(UriComponentsBuilder
                .fromPath(BookRouterConfig.BOOK_PATH)
                .queryParam("bookIsbn",isbn).build().toUri())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.size()").value(equalTo(1));

    }

    public BookDTO getSavedTestBook(){
        FluxExchangeResult<BookDTO> bookDTOFluxExchangeResult = webTestClient.post().uri(BookRouterConfig.BOOK_PATH)
                .body(Mono.just(BookServiceImplTest.getTestBook()), BookDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(BookDTO.class);

      //  List<String> location = bookDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient.get().uri(BookRouterConfig.BOOK_PATH)
                .exchange().returnResult(BookDTO.class).getResponseBody().blockFirst();
    }

}