package noob.reactivemongo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Book {

    @Id
    private String bookId;
    private Integer version;
    private String bookName;
    private String bookIsbn;
    private String publisher;
    private BigDecimal price;
    private Integer quantityOnHand;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;
}
