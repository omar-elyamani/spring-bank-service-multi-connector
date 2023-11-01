package ma.formations.graphql;

import ma.formations.graphql.dtos.bankaccount.AddBankAccountRequest;
import ma.formations.graphql.dtos.bankaccount.BankAccountConverter;
import ma.formations.graphql.service.model.BankAccount;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestBankAccountConverter {
    @Autowired
    private BankAccountConverter bankAccountConverter;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void test1() {

        AddBankAccountRequest dto = AddBankAccountRequest.builder().
                customerIdentityRef("FA66962").
                rib("RIB_13").
                amount(15000.0).
                build();
        BankAccount bo = bankAccountConverter.AddBankAccountRequestToBankAccount(dto);
        assertThat(bo.getCustomer()).isNotNull();
        assertThat(bo.getCustomer().getIdentityRef()).isEqualTo(dto.getCustomerIdentityRef());
    }
}

