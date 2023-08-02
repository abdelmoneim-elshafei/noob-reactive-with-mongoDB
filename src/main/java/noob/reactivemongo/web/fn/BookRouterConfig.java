package noob.reactivemongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class BookRouterConfig {
    public static final String BOOK_PATH = "/api/book";
    public static final String BOOK_PATH_ID = BOOK_PATH + "/{bookId}";
    private final BookHandler bookHandler;
    @Bean
    public RouterFunction<ServerResponse> bookRoutes(){
        return route()
                .GET(BOOK_PATH, accept(MediaType.APPLICATION_JSON),bookHandler::listBook)
                .GET(BOOK_PATH_ID,accept(MediaType.APPLICATION_JSON),bookHandler::getById)
                .POST(BOOK_PATH,accept(MediaType.APPLICATION_JSON),bookHandler::createNewBook)
                .PUT(BOOK_PATH_ID,accept(MediaType.APPLICATION_JSON),bookHandler::updateBook)
                .PATCH(BOOK_PATH_ID,accept(MediaType.APPLICATION_JSON),bookHandler::patchBook)
                .DELETE(BOOK_PATH_ID,accept(MediaType.APPLICATION_JSON),bookHandler::deleteBook)
                .build();

    }

}
