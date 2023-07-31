package noob.reactivemongo.service.Impl;

import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import noob.reactivemongo.mapper.BookMapper;
import noob.reactivemongo.model.BookDTO;
import noob.reactivemongo.repository.BookRepository;
import noob.reactivemongo.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Flux<BookDTO> getAllBooks() {
        return bookRepository.findAll().map(bookMapper::bookToBookDto);
    }

    @Override
    public Mono<BookDTO> saveBook(Mono<BookDTO> bookDTO) {
        return bookDTO.map(bookMapper::bookDtoToBook)
                .flatMap(bookRepository::save)
                .map(bookMapper::bookToBookDto);
    }

    @Override
    public Mono<BookDTO> saveBook(BookDTO bookDTO) {
        return bookRepository.save(bookMapper.bookDtoToBook(bookDTO))
                .map(bookMapper::bookToBookDto);
    }

    @Override
    public Mono<BookDTO> getBookById(String id) {
        return bookRepository.findById(id).map(bookMapper::bookToBookDto);
    }

    @Override
    public Mono<BookDTO> updateBookById(String id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(found ->{
                    found.setBookName(bookDTO.getBookName());
                    found.setBookIsbn(bookDTO.getBookIsbn());
                    found.setPrice(bookDTO.getPrice());
                    found.setPublisher(bookDTO.getPublisher());
                    found.setVersion(bookDTO.getVersion());
                    found.setUpdateTime(LocalDateTime.now());
                    found.setQuantityOnHand(bookDTO.getQuantityOnHand());
                    return found;
                }).flatMap(bookRepository::save)
                .map(bookMapper::bookToBookDto);
    }

    @Override
    public Mono<BookDTO> patchBookById(String id, BookDTO bookDTO) {
            return bookRepository.findById(id)
                .map(found ->{
                   if(StringUtils.hasText(bookDTO.getBookName()))
                        found.setBookName(bookDTO.getBookName());

                   if(StringUtils.hasText(bookDTO.getBookIsbn()))
                        found.setBookIsbn(bookDTO.getBookIsbn());

                   if(bookDTO.getPrice() != null)
                        found.setPrice(bookDTO.getPrice());

                   if(StringUtils.hasText(bookDTO.getPublisher()))
                        found.setPublisher(bookDTO.getPublisher());

                   if(bookDTO.getVersion() != null)
                        found.setVersion(bookDTO.getVersion());

                    found.setUpdateTime(LocalDateTime.now());

                    if(bookDTO.getQuantityOnHand() != null)
                        found.setQuantityOnHand(bookDTO.getQuantityOnHand());
                    return found;
                }).flatMap(bookRepository::save)
                .map(bookMapper::bookToBookDto);
    }

    @Override
    public Mono<Void> deleteBookById(String id) {
        return bookRepository.deleteById(id);
    }
}
