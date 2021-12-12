package subscription.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubscriptionDTO {

    @DecimalMin("1")
    @DecimalMax("20")
    private int id_category;

}
