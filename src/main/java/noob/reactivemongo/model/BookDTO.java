package noob.reactivemongo.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BookDTO {

    private String bookId;
    private String bookName;
    private String bookIsbn;
    private String publisher;
    private Integer version;
    private BigDecimal price;
    private Integer quantityOnHand;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;
}
