package noob.reactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import noob.reactivemongo.model.BookDTO;
import noob.reactivemongo.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BookHandler {
    private final BookService bookService;
    private final Validator validator;

    private void validate(BookDTO bookDTO){
        Errors errors = new BeanPropertyBindingResult(bookDTO,"bookDto");
        validator.validate(bookDTO,errors);
        if(errors.hasErrors()){
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> listBook(ServerRequest serverRequest){
        Flux<BookDTO> flux;
        if(serverRequest.queryParam("bookIsbn").isPresent()){
            flux = bookService.findBookByIsbn(serverRequest.queryParam("bookIsbn").get());
        }else {
            flux = bookService.getAllBooks();
        }
        return ServerResponse.ok()
                .body(flux, BookDTO.class);
    }
    public Mono<ServerResponse> getById(ServerRequest serverRequest){
        return ServerResponse.ok()
                .body(bookService.getBookById(serverRequest.pathVariable("bookId"))
                        .doOnNext(this::validate)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),BookDTO.class);
    }
    public Mono<ServerResponse> createNewBook(ServerRequest serverRequest){
        return bookService.saveBook(serverRequest.bodyToMono(BookDTO.class).doOnNext(this::validate))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(bookDTO -> ServerResponse.created(UriComponentsBuilder
                        .fromPath(BookRouterConfig.BOOK_PATH_ID).build(bookDTO.getBookId()))
                        .build());

    }
    public Mono<ServerResponse> updateBook(ServerRequest serverRequest){
        return serverRequest.bodyToMono(BookDTO.class)
                .doOnNext(this::validate)
                .flatMap(bookDTO -> bookService.updateBookById(serverRequest.pathVariable("bookId"),bookDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(saved -> ServerResponse.noContent().build());

    }
    public Mono<ServerResponse> patchBook(ServerRequest serverRequest){
        return serverRequest.bodyToMono(BookDTO.class)
                .doOnNext(this::validate)
                .flatMap(bookDTO -> bookService.patchBookById(serverRequest.pathVariable("bookId"),bookDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(saved -> ServerResponse.noContent().build());

    }

    public Mono<ServerResponse> deleteBook(ServerRequest serverRequest){
        return bookService.getBookById(serverRequest.pathVariable("bookId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(bookDTO -> bookService.deleteBookById(bookDTO.getBookId()))
                .then(ServerResponse.noContent().build());
    }

}
