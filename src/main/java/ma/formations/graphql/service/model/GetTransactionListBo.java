package ma.formations.graphql.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetTransactionListBo {
    private String rib;
    private Date dateTo;
    private Date dateFrom;
}
