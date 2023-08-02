package noob.reactivemongo.repository;

import noob.reactivemongo.domain.Book;
import noob.reactivemongo.model.BookDTO;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveMongoRepository<Book,String> {
    Flux<Book> findBooksByBookIsbn(String isbn);

}

