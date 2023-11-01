package ma.formations.graphql.service;

import ma.formations.graphql.dtos.customer.AddCustomerRequest;
import ma.formations.graphql.dtos.customer.AddCustomerResponse;
import ma.formations.graphql.dtos.customer.CustomerDto;

import java.util.List;

public interface ICustomerService {

    List<CustomerDto> getAllCustomers();

    AddCustomerResponse createCustomer(AddCustomerRequest addCustomerRequest);

    CustomerDto getCustomByIdentity(String identity);


}
