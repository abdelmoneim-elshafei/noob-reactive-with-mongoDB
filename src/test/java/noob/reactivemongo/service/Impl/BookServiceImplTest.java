package noob.reactivemongo.service.Impl;

import noob.reactivemongo.domain.Book;
import noob.reactivemongo.mapper.BookMapper;
import noob.reactivemongo.model.BookDTO;
import noob.reactivemongo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;
    BookDTO bookDTO;
    @BeforeEach
    void setUp() {
        bookDTO = bookMapper.bookToBookDto(testBook());
    }

    @Test
    void saveBook()  {
      AtomicBoolean atomicBoolean = new AtomicBoolean(false);
      Mono<BookDTO> book = bookService.saveBook(Mono.just(bookDTO));
      book.subscribe(bookDTO1 -> {
          System.out.println(bookDTO1.getBookId());
          atomicBoolean.set(true);
      });
        //Awaitility
        await().untilTrue(atomicBoolean);
    }
    public static Book testBook(){
        return Book.builder()
                .bookName("c++")
                .bookIsbn("343-4343434")
                .price(new BigDecimal("34.3"))
                .version(2)
                .publisher("Orily")
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .quantityOnHand(34)
                .build();
    }
}