package ma.formations.graphql.service;

import ma.formations.graphql.dtos.customer.*;

import java.util.List;

public interface ICustomerService {
    List<CustomerDto> getAllCustomers();
    AddCustomerResponse createCustomer(AddCustomerRequest addCustomerRequest);
    UpdateCustomerResponse updateCustomer(String identityRef, UpdateCustomerRequest updateCustomerRequest);
    CustomerDto getCustomByIdentity(String identity);
    String deleteCustomerByIdentityRef(String identityRef);
}
