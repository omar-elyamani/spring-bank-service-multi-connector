package ma.formations.multiconnector;

import ma.formations.multiconnector.dtos.bankaccount.AddBankAccountRequest;
import ma.formations.multiconnector.service.model.BankAccount;
import ma.formations.multiconnector.service.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestBankAccountConverter {
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        // Custom mapping for the Customer field if necessary
        modelMapper.addMappings(new PropertyMap<AddBankAccountRequest, BankAccount>() {
            @Override
            protected void configure() {
                map().getCustomer().setIdentityRef(source.getCustomerIdentityRef());
            }
        });
    }

    @Test
    void test1() {
        // Arrange: Create a DTO
        AddBankAccountRequest dto = AddBankAccountRequest.builder()
                .customerIdentityRef("FA66962")
                .rib("RIB_13")
                .amount(15000.0)
                .build();

        // Act: Map DTO to BO
        BankAccount bo = modelMapper.map(dto, BankAccount.class);

        // Assert: Verify the mapped fields
        assertThat(bo).isNotNull(); // Ensure the BankAccount object is not null
        assertThat(bo.getCustomer()).isNotNull(); // Ensure the Customer object is not null
        assertThat(bo.getCustomer().getIdentityRef()).isEqualTo(dto.getCustomerIdentityRef()); // Check identityRef
        assertThat(bo.getRib()).isEqualTo(dto.getRib()); // Check RIB
        assertThat(bo.getAmount()).isEqualTo(dto.getAmount()); // Check amount
    }
}