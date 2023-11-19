package ma.formations.graphql.presentation;

import lombok.AllArgsConstructor;
import ma.formations.graphql.common.CommonTools;
import ma.formations.graphql.dtos.transaction.AddWirerTransferRequest;
import ma.formations.graphql.dtos.transaction.AddWirerTransferResponse;
import ma.formations.graphql.dtos.transaction.GetTransactionListRequest;
import ma.formations.graphql.dtos.transaction.TransactionDto;
import ma.formations.graphql.service.ITransactionService;
import ma.formations.graphql.service.exception.BusinessException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
@AllArgsConstructor

public class TransactionGraphqlController {

    private ITransactionService transactionService;
    private CommonTools commonTools;

    @MutationMapping
    public AddWirerTransferResponse addWirerTransfer(@Argument("dto") AddWirerTransferRequest dto) {
        return transactionService.wiredTransfer(dto);
    }

    @QueryMapping
    public List<TransactionDto> getTransactions(@Argument GetTransactionListRequest dto) {
        return transactionService.getTransactions(dto);
    }
}
