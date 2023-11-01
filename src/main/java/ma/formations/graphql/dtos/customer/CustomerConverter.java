package ma.formations.graphql.dtos.customer;

import lombok.AllArgsConstructor;
import ma.formations.graphql.service.model.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CustomerConverter {

    private final ModelMapper modelMapper;

    public AddCustomerResponse customerToAddCustomerResponse(Customer bo) {
        return modelMapper.map(bo, AddCustomerResponse.class);
    }

    public List<CustomerDto> customerDtos(List<Customer> boList) {
        return boList.stream().map(this::customerToCustomerDTO).toList();
    }

    public CustomerDto customerToCustomerDTO(Customer bo) {
        return modelMapper.map(bo, CustomerDto.class);
    }

    public Customer addCustomerRequestToCustomer(AddCustomerRequest dto) {
        return modelMapper.map(dto, Customer.class);
    }
}
