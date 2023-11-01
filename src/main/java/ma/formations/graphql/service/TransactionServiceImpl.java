package ma.formations.graphql.service;

import lombok.AllArgsConstructor;
import ma.formations.graphql.dao.BankAccountRepository;
import ma.formations.graphql.dao.BankAccountTransactionRepository;
import ma.formations.graphql.dao.UserRepository;
import ma.formations.graphql.dtos.transaction.AddWirerTransferRequest;
import ma.formations.graphql.dtos.transaction.AddWirerTransferResponse;
import ma.formations.graphql.dtos.transaction.TransactionConverter;
import ma.formations.graphql.dtos.transaction.TransactionDto;
import ma.formations.graphql.enums.AccountStatus;
import ma.formations.graphql.enums.TransactionType;
import ma.formations.graphql.service.exception.BusinessException;
import ma.formations.graphql.service.model.BankAccount;
import ma.formations.graphql.service.model.BankAccountTransaction;
import ma.formations.graphql.service.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountTransactionRepository bankAccountTransactionRepository;

    private final UserRepository userRepository;

    private TransactionConverter transactionConverter;


    @Override
    public AddWirerTransferResponse wiredTransfer(AddWirerTransferRequest dto) {

        BankAccountTransaction transactionFrom = transactionConverter.toTransactionFrom(dto);
        BankAccountTransaction transactionTo = transactionConverter.toTransactionTo(dto);
        String username = transactionFrom.getUser().getUsername();
        String ribFrom = transactionFrom.getBankAccount().getRib();
        String ribTo = transactionTo.getBankAccount().getRib();
        Double amount = transactionFrom.getAmount();

        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new BusinessException(String.format("User [%s] doesn't exist", username)));

        BankAccount bankAccountFrom = bankAccountRepository.findByRib(ribFrom).
                orElseThrow(() -> new BusinessException(String.format("No bank account have the rib %s", ribFrom)));

        BankAccount bankAccountTo = bankAccountRepository.findByRib(ribTo).
                orElseThrow(() -> new BusinessException(String.format("No bank account have the rib %s", ribTo)));

        checkBusinessRules(bankAccountFrom, bankAccountTo, amount);
        //On débite le compte demandeur
        bankAccountFrom.setAmount(bankAccountFrom.getAmount() - amount);
        //On crédite le compte destinataire
        bankAccountTo.setAmount(bankAccountTo.getAmount() + amount);
        Date now = new Date();
        transactionFrom.setTransactionType(TransactionType.DEBIT);
        transactionFrom.setCreatedAt(now);
        transactionFrom.setUser(user);
        transactionFrom.setBankAccount(bankAccountFrom);
        transactionTo.setTransactionType(TransactionType.CREDIT);
        transactionTo.setCreatedAt(now);
        transactionTo.setUser(user);
        transactionTo.setBankAccount(bankAccountTo);
        bankAccountTransactionRepository.save(transactionFrom);
        bankAccountTransactionRepository.save(transactionTo);
        return transactionConverter.toAddWirerTransferResponse(transactionFrom, transactionTo);
    }

    private void checkBusinessRules(BankAccount bankAccountFrom, BankAccount bankAccountTo, Double amount) {

        if (bankAccountFrom.getAccountStatus().equals(AccountStatus.CLOSED))
            throw new BusinessException(String.format("the bank account %s is closed !!", bankAccountFrom.getRib()));

        if (bankAccountFrom.getAccountStatus().equals(AccountStatus.BLOCKED))
            throw new BusinessException(String.format("the bank account %s is blocked !!", bankAccountFrom.getRib()));

        if (bankAccountTo.getAccountStatus().equals(AccountStatus.CLOSED))
            throw new BusinessException(String.format("the bank account %s is closed !!", bankAccountTo.getRib()));

        if (bankAccountTo.getAccountStatus().equals(AccountStatus.BLOCKED))
            throw new BusinessException(String.format("the bank account %s is blocked !!", bankAccountTo.getRib()));

        if (bankAccountFrom.getAmount() < amount)
            throw new BusinessException(String.format("the balance of account number %s is less than %s", bankAccountFrom.getRib(), amount));
    }


    @Override
    public List<TransactionDto> getTransactions(String rib, Date dateFrom, Date dateTo) {
        return transactionConverter.transactionDtos(bankAccountTransactionRepository.findByBankAccount_RibAndCreatedAtBetween(rib, dateFrom, dateTo));
    }
}
