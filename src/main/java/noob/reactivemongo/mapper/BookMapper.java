package noob.reactivemongo.mapper;

import noob.reactivemongo.domain.Book;
import noob.reactivemongo.model.BookDTO;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookMapper() {
    }

    public Book bookDtoToBook(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        } else {
            Book.BookBuilder book = Book.builder();
            book.bookId(bookDTO.getBookId());
            book.version(bookDTO.getVersion());
            book.bookName(bookDTO.getBookName());
            book.bookIsbn(bookDTO.getBookIsbn());
            book.publisher(bookDTO.getPublisher());
            book.price(bookDTO.getPrice());
            book.quantityOnHand(bookDTO.getQuantityOnHand());
            book.createdTime(bookDTO.getCreatedTime());
            book.updateTime(bookDTO.getUpdateTime());
            return book.build();
        }
    }

    public BookDTO bookToBookDto(Book book) {
        if (book == null) {
            return null;
        } else {
            BookDTO.BookDTOBuilder bookDTO = BookDTO.builder();
            bookDTO.bookId(book.getBookId());
            bookDTO.bookName(book.getBookName());
            bookDTO.bookIsbn(book.getBookIsbn());
            bookDTO.publisher(book.getPublisher());
            bookDTO.version(book.getVersion());
            bookDTO.price(book.getPrice());
            bookDTO.quantityOnHand(book.getQuantityOnHand());
            bookDTO.createdTime(book.getCreatedTime());
            bookDTO.updateTime(book.getUpdateTime());
            return bookDTO.build();
        }
    }
}
