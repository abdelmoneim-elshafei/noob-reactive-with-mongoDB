package noob.reactivemongo.repository;

import noob.reactivemongo.domain.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookRepository extends ReactiveMongoRepository<Book,String> {

}

