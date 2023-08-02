package noob.reactivemongo.service;

import noob.reactivemongo.model.BookDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {

    Flux<BookDTO> getAllBooks();
    Mono<BookDTO> saveBook(Mono<BookDTO> bookDTO);
    Mono<BookDTO> saveBook(BookDTO bookDTO);
    Mono<BookDTO> getBookById(String id);
    Mono<BookDTO> updateBookById(String id,BookDTO bookDTO);
    Mono<BookDTO> patchBookById(String id,BookDTO bookDTO);
    Mono<Void> deleteBookById(String id);
    Flux<BookDTO> findBookByIsbn(String isbn);


}
