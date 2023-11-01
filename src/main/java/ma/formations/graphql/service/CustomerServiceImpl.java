package ma.formations.graphql.service;

import lombok.AllArgsConstructor;
import ma.formations.graphql.dao.CustomerRepository;
import ma.formations.graphql.dtos.customer.AddCustomerRequest;
import ma.formations.graphql.dtos.customer.AddCustomerResponse;
import ma.formations.graphql.dtos.customer.CustomerConverter;
import ma.formations.graphql.dtos.customer.CustomerDto;
import ma.formations.graphql.service.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerConverter customerConverter;

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerConverter.customerDtos(customerRepository.findAll());
    }


    @Override
    public AddCustomerResponse createCustomer(AddCustomerRequest addCustomerRequest) {
        AddCustomerResponse response = customerConverter.customerToAddCustomerResponse(
                customerRepository.save(customerConverter.addCustomerRequestToCustomer(addCustomerRequest)));
        response.setMessage(String.format("Customer : [identity= %s,First Name= %s, Last Name= %s, username= %s] was created with success",
                response.getIdentityRef(), response.getFirstname(), response.getLastname(), response.getUsername()));
        return response;
    }

    @Override
    public CustomerDto getCustomByIdentity(String identity) {
        return customerConverter.customerToCustomerDTO(customerRepository.findByIdentityRef(identity).orElseThrow(
                () -> new BusinessException(String.format("No Customer with identity [%s] exist ", identity))));
    }

}
