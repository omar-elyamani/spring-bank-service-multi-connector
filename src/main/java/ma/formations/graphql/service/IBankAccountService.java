package ma.formations.graphql.service;

import ma.formations.graphql.dtos.bankaccount.AddBankAccountRequest;
import ma.formations.graphql.dtos.bankaccount.AddBankAccountResponse;
import ma.formations.graphql.dtos.bankaccount.BankAccountDto;

import java.util.List;

public interface IBankAccountService {
    AddBankAccountResponse saveBankAccount(AddBankAccountRequest dto);
    List<BankAccountDto> getAllBankAccounts();
    BankAccountDto getBankAccountByRib(String rib);
}
