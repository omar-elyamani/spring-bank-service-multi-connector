package ma.formations.graphql.presentation;

import ma.formations.graphql.dtos.customer.AddCustomerRequest;
import ma.formations.graphql.dtos.customer.AddCustomerResponse;
import ma.formations.graphql.dtos.customer.CustomerDto;
import ma.formations.graphql.service.ICustomerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CustomerGraphqlController {

    private final ICustomerService customerService;

    public CustomerGraphqlController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @QueryMapping
    List<CustomerDto> customers() {
        return customerService.getAllCustomers();
    }

    @QueryMapping
    CustomerDto customerByIdentity(@Argument String identity) {
        return customerService.getCustomByIdentity(identity);
    }

    @MutationMapping
    public AddCustomerResponse createCustomer(@Argument("dto") AddCustomerRequest dto) {
        return customerService.createCustomer(dto);
    }

}
