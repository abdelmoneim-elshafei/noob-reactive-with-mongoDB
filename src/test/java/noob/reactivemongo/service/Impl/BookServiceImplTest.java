package noob.reactivemongo.service.Impl;

import noob.reactivemongo.domain.Book;
import noob.reactivemongo.mapper.BookMapper;
import noob.reactivemongo.model.BookDTO;
import noob.reactivemongo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceImplTest {

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
    @DisplayName("test save book by subscribe")
    void saveBookBySubscribe() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BookDTO> atomicReference = new AtomicReference<>();

        Mono<BookDTO> book = bookService.saveBook(Mono.just(bookDTO));
        book.subscribe(bookDTO1 -> {
            System.out.println(bookDTO1.getBookId());
            atomicReference.set(bookDTO1);
            atomicBoolean.set(true);
        });
        //Awaitility
        await().untilTrue(atomicBoolean);
        BookDTO prBook = atomicReference.get();
        assertThat(prBook).isNotNull();
        assertThat(prBook.getBookId()).isNotNull();
    }

    @Test
    @DisplayName("test save book by block")
    void saveBookByBlock() {
        BookDTO bookDTO1 = bookService.saveBook(Mono.just(getTestBook())).block();
        assertThat(bookDTO1).isNotNull();
        assertThat(bookDTO1.getBookId()).isNotNull();

    }


    @Test
    void deleteBook() {
        BookDTO bookDTO1 = getSavedDto();
        bookService.deleteBookById(bookDTO1.getBookId()).block();
        Mono<BookDTO> bookDTOMono = bookService.getBookById(bookDTO1.getBookId());
        BookDTO bookDTO2 = bookDTOMono.block();
        assertThat(bookDTO2).isNull();
        
    }
    public BookDTO getSavedDto() {
        return bookService.saveBook(Mono.just(getTestBook())).block();
    }
    public static BookDTO getTestBook() {
        return new BookMapper().bookToBookDto(testBook());
    }


    public static Book testBook() {
        return Book.builder()
                .bookName("c++")
                .bookIsbn("343-4343434-432")
                .price(new BigDecimal("34.3"))
                .version(2)
                .publisher("Orily")
                .createdTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .quantityOnHand(34)
                .build();
    }
}