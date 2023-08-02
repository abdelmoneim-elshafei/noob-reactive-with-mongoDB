package noob.reactivemongo.web.fn;

import noob.reactivemongo.model.BookDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
public class EndPointTest {

    @Autowired
    WebTestClient wtc;

    @Test
    void getAllBook() {
        wtc.get().uri(BookRouterConfig.BOOK_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }
    @Test
    void getBookById() {
        wtc.get().uri(BookRouterConfig.BOOK_PATH_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }
}
