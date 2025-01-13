package ma.formations.multiconnector.service;

import lombok.AllArgsConstructor;
import ma.formations.multiconnector.dao.BankAccountRepository;
import ma.formations.multiconnector.dao.CustomerRepository;
import ma.formations.multiconnector.dtos.bankaccount.AddBankAccountRequest;
import ma.formations.multiconnector.dtos.bankaccount.AddBankAccountResponse;
import ma.formations.multiconnector.dtos.bankaccount.BankAccountDto;
import ma.formations.multiconnector.enums.AccountStatus;
import ma.formations.multiconnector.service.exception.BusinessException;
import ma.formations.multiconnector.service.model.BankAccount;
import ma.formations.multiconnector.service.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImpl implements IBankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private ModelMapper modelMapper;


    @Override
    public AddBankAccountResponse saveBankAccount(AddBankAccountRequest dto) {
        // Configure ModelMapper to skip mapping for the customer field
        modelMapper.typeMap(AddBankAccountRequest.class, BankAccount.class)
                .addMappings(mapper -> mapper.skip(BankAccount::setCustomer));

        // Map the rest of the fields from DTO to the entity
        BankAccount bankAccount = modelMapper.map(dto, BankAccount.class);

        // Manually retrieve and set the customer
        Customer customer = customerRepository.findByIdentityRef(dto.getCustomerIdentityRef())
                .orElseThrow(() -> new BusinessException(
                        String.format("No customer with identity: %s exists", dto.getCustomerIdentityRef())
                ));
        bankAccount.setCustomer(customer);

        // Set additional properties
        bankAccount.setAccountStatus(AccountStatus.OPENED);
        bankAccount.setCreatedAt(new Date());

        // Save and map the response
        AddBankAccountResponse response = modelMapper.map(bankAccountRepository.save(bankAccount), AddBankAccountResponse.class);
        response.setMessage(String.format("RIB [%s] for customer [%s] created successfully",
                dto.getRib(), dto.getCustomerIdentityRef()));
        return response;
    }


    @Override
    public List<BankAccountDto> getAllBankAccounts() {
        return bankAccountRepository.findAll().stream().
                map(bankAccount -> modelMapper.map(bankAccount, BankAccountDto.class)).
                collect(Collectors.toList());
    }

    @Override
    public BankAccountDto getBankAccountByRib(String rib) {
        return modelMapper.map(bankAccountRepository.findByRib(rib).orElseThrow(
                () -> new BusinessException(String.format("No Bank Account with rib [%s] exist", rib))), BankAccountDto.class);
    }
}
