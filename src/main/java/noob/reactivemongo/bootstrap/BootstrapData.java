package noob.reactivemongo.bootstrap;


import lombok.AllArgsConstructor;
import noob.reactivemongo.domain.Book;
import noob.reactivemongo.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BookRepository bookRepository;
    @Override
    public void run(String... args) throws Exception {
        loadData();

        bookRepository.count().subscribe(count ->{
            System.out.println("count is : " + count);
        });
    }
    void loadData(){
        bookRepository.count().subscribe(count ->{
            if (count == 0){
                Book book1 = Book.builder()
                        .bookName("The Great Gatsby")
                        .bookIsbn("978-3-16-148410-0")
                        .publisher("Scribner")
                        .price(new BigDecimal("10.99"))
                        .quantityOnHand(100)
                        .createdTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                Book book2 = Book.builder()
                        .bookName("To Kill a Mockingbird")
                        .bookIsbn("978-0-06-112008-4")
                        .publisher("HarperCollins")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(50)
                        .createdTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
                Book book3 = Book.builder()
                        .bookName("1984")
                        .bookIsbn("978-0-14-103614-4")
                        .publisher("Penguin Books")
                        .price(new BigDecimal("8.99"))
                        .quantityOnHand(200)
                        .createdTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();

                bookRepository.save(book1).subscribe();
                bookRepository.save(book2).subscribe();
                bookRepository.save(book3).subscribe();
            }
        });

    }
}
