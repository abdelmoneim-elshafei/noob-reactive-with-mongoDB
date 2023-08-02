package noob.reactivemongo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BookDTO {

    private String bookId;

    @NotBlank
    @Size(min = 3, max = 255)
    private String bookName;

    @Size(min = 13, max = 25)
    private String bookIsbn;
    private String publisher;
    private Integer version;
    private BigDecimal price;
    private Integer quantityOnHand;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;
}
