package ma.formations.graphql.service;

import ma.formations.graphql.dtos.transaction.AddWirerTransferRequest;
import ma.formations.graphql.dtos.transaction.AddWirerTransferResponse;
import ma.formations.graphql.dtos.transaction.GetTransactionListRequest;
import ma.formations.graphql.dtos.transaction.TransactionDto;

import java.util.Date;
import java.util.List;
public interface ITransactionService {
    AddWirerTransferResponse wiredTransfer(AddWirerTransferRequest dto);
    List<TransactionDto> getTransactions(GetTransactionListRequest dto);
}
