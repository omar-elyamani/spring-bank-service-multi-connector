package ma.formations.graphql.dtos.bankaccount;

import lombok.AllArgsConstructor;
import ma.formations.graphql.service.model.BankAccount;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class BankAccountConverter {

    private ModelMapper modelMapper;

    public BankAccountDto bankAccountToBankAccountDTO(BankAccount bo) {
        return modelMapper.map(bo, BankAccountDto.class);
    }

    public List<BankAccountDto> bankAccountDtos(List<BankAccount> boList) {
        return boList.stream().map(this::bankAccountToBankAccountDTO).toList();
    }

    public AddBankAccountResponse bankAccountToAddBankAccountResponse(BankAccount bo) {
        return modelMapper.map(bo, AddBankAccountResponse.class);
    }

    public BankAccount AddBankAccountRequestToBankAccount(AddBankAccountRequest dto) {
        return modelMapper.map(dto, BankAccount.class);
    }

}
