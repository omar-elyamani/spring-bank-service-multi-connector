package ma.formations.multiconnector;

import ma.formations.multiconnector.dtos.bankaccount.AddBankAccountRequest;
import ma.formations.multiconnector.dtos.customer.AddCustomerRequest;
import ma.formations.multiconnector.dtos.transaction.AddWirerTransferRequest;
import ma.formations.multiconnector.dtos.user.PermissionVo;
import ma.formations.multiconnector.dtos.user.RoleVo;
import ma.formations.multiconnector.dtos.user.UserVo;
import ma.formations.multiconnector.enums.Permissions;
import ma.formations.multiconnector.enums.Roles;
import ma.formations.multiconnector.service.IBankAccountService;
import ma.formations.multiconnector.service.ICustomerService;
import ma.formations.multiconnector.service.ITransactionService;
import ma.formations.multiconnector.service.IUserService;
import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class BankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankServiceApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GrpcAuthenticationReader grpcAuthenticationReader() {
        return new BasicGrpcAuthenticationReader();
    }

    @Bean
    CommandLineRunner initDataBase(ICustomerService customerService,
                                   IBankAccountService bankAccountService,
                                   ITransactionService transactionService,
                                   IUserService userService) {
        return args -> {
            // Create customers
            customerService.createCustomer(AddCustomerRequest.builder()
                    .username("user1")
                    .identityRef("A100")
                    .firstname("FIRST_NAME1")
                    .lastname("LAST_NAME1")
                    .build());

            bankAccountService.saveBankAccount(AddBankAccountRequest.builder()
                    .rib("RIB_1")
                    .amount(1000000d)
                    .customerIdentityRef("A100")
                    .build());

            bankAccountService.saveBankAccount(AddBankAccountRequest.builder()
                    .rib("RIB_11")
                    .amount(2000000d)
                    .customerIdentityRef("A100")
                    .build());

            customerService.createCustomer(AddCustomerRequest.builder()
                    .username("user2")
                    .identityRef("A200")
                    .firstname("FIRST_NAME2")
                    .lastname("LAST_NAME2")
                    .build());

            bankAccountService.saveBankAccount(AddBankAccountRequest.builder()
                    .rib("RIB_2")
                    .amount(2000000d)
                    .customerIdentityRef("A200")
                    .build());

            customerService.createCustomer(AddCustomerRequest.builder()
                    .username("user3")
                    .identityRef("A900")
                    .firstname("FIRST_NAME9")
                    .lastname("LAST_NAME9")
                    .build());

            bankAccountService.saveBankAccount(AddBankAccountRequest.builder()
                    .rib("RIB_9")
                    .amount(-25000d)
                    .customerIdentityRef("A900")
                    .build());

            customerService.createCustomer(AddCustomerRequest.builder()
                    .username("user4")
                    .identityRef("A800")
                    .firstname("FIRST_NAME8")
                    .lastname("LAST_NAME8")
                    .build());

            bankAccountService.saveBankAccount(AddBankAccountRequest.builder()
                    .rib("RIB_8")
                    .amount(0.0)
                    .customerIdentityRef("A800")
                    .build());

            // Perform wire transfers
            transactionService.wiredTransfer(AddWirerTransferRequest.builder()
                    .ribFrom("RIB_1")
                    .ribTo("RIB_2")
                    .amount(10000.0)
                    .username("user1")
                    .build());

            transactionService.wiredTransfer(AddWirerTransferRequest.builder()
                    .ribFrom("RIB_1")
                    .ribTo("RIB_9")
                    .amount(20000.0)
                    .username("user1")
                    .build());

            transactionService.wiredTransfer(AddWirerTransferRequest.builder()
                    .ribFrom("RIB_1")
                    .ribTo("RIB_8")
                    .amount(500.0)
                    .username("user1")
                    .build());

            transactionService.wiredTransfer(AddWirerTransferRequest.builder()
                    .ribFrom("RIB_2")
                    .ribTo("RIB_11")
                    .amount(300.0)
                    .username("user2")
                    .build());

            // Add all permissions
            Arrays.stream(Permissions.values()).forEach(permission ->
                    userService.save(PermissionVo.builder().authority(permission.name()).build()));

            // Create roles
            RoleVo roleAgentGuichet = RoleVo.builder()
                    .authority(Roles.ROLE_AGENT_GUICHET.name())
                    .authorities(List.of(
                            userService.getPermissionByName(Permissions.GET_ALL_CUSTOMERS.name()),
                            userService.getPermissionByName(Permissions.GET_CUSTOMER_BY_IDENTITY.name()),
                            userService.getPermissionByName(Permissions.CREATE_CUSTOMER.name()),
                            userService.getPermissionByName(Permissions.UPDATE_CUSTOMER.name()),
                            userService.getPermissionByName(Permissions.DELETE_CUSTOMER.name()),
                            userService.getPermissionByName(Permissions.GET_ALL_BANK_ACCOUNT.name()),
                            userService.getPermissionByName(Permissions.GET_BANK_ACCOUNT_BY_RIB.name()),
                            userService.getPermissionByName(Permissions.CREATE_BANK_ACCOUNT.name()),
                            userService.getPermissionByName(Permissions.DELETE_BANK_ACCOUNT.name())))
                    .build();

            RoleVo roleAgentGuichetGet = RoleVo.builder()
                    .authority(Roles.ROLE_AGENT_GUICHET_GET.name())
                    .authorities(List.of(
                            userService.getPermissionByName(Permissions.GET_ALL_CUSTOMERS.name()),
                            userService.getPermissionByName(Permissions.GET_CUSTOMER_BY_IDENTITY.name()),
                            userService.getPermissionByName(Permissions.GET_ALL_BANK_ACCOUNT.name()),
                            userService.getPermissionByName(Permissions.GET_BANK_ACCOUNT_BY_RIB.name())))
                    .build();

            RoleVo roleClient = RoleVo.builder()
                    .authority(Roles.ROLE_CLIENT.name())
                    .authorities(List.of(
                            userService.getPermissionByName(Permissions.GET_CUSTOMER_BY_IDENTITY.name()),
                            userService.getPermissionByName(Permissions.GET_CUSTOMER_BANK_ACCOUNTS.name()),
                            userService.getPermissionByName(Permissions.GET_BANK_ACCOUNT_BY_RIB.name()),
                            userService.getPermissionByName(Permissions.ADD_WIRED_TRANSFER.name()),
                            userService.getPermissionByName(Permissions.GET_TRANSACTIONS.name())))
                    .build();

            RoleVo roleAdmin = RoleVo.builder()
                    .authority(Roles.ROLE_ADMIN.name())
                    .authorities(List.of(
                            userService.getPermissionByName(Permissions.GET_ALL_CUSTOMERS.name()),
                            userService.getPermissionByName(Permissions.GET_CUSTOMER_BY_IDENTITY.name()),
                            userService.getPermissionByName(Permissions.CREATE_CUSTOMER.name()),
                            userService.getPermissionByName(Permissions.UPDATE_CUSTOMER.name()),
                            userService.getPermissionByName(Permissions.DELETE_CUSTOMER.name()),
                            userService.getPermissionByName(Permissions.GET_ALL_BANK_ACCOUNT.name()),
                            userService.getPermissionByName(Permissions.GET_BANK_ACCOUNT_BY_RIB.name()),
                            userService.getPermissionByName(Permissions.CREATE_BANK_ACCOUNT.name()),
                            userService.getPermissionByName(Permissions.DELETE_BANK_ACCOUNT.name()),
                            userService.getPermissionByName(Permissions.GET_CUSTOMER_BY_IDENTITY.name()),
                            userService.getPermissionByName(Permissions.GET_CUSTOMER_BANK_ACCOUNTS.name()),
                            userService.getPermissionByName(Permissions.GET_BANK_ACCOUNT_BY_RIB.name()),
                            userService.getPermissionByName(Permissions.ADD_WIRED_TRANSFER.name()),
                            userService.getPermissionByName(Permissions.GET_TRANSACTIONS.name())))
                    .build();

            userService.save(roleAgentGuichet);
            userService.save(roleAgentGuichetGet);
            userService.save(roleClient);
            userService.save(roleAdmin);

            // Create users
            userService.save(UserVo.builder()
                    .username("agentguichet")
                    .password("agentguichet")
                    .authorities(List.of(roleAgentGuichet))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());

            userService.save(UserVo.builder()
                    .username("agentguichet2")
                    .password("agentguichet2")
                    .authorities(List.of(roleAgentGuichetGet))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());

            userService.save(UserVo.builder()
                    .username("client")
                    .password("client")
                    .authorities(List.of(roleClient))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());

            userService.save(UserVo.builder()
                    .username("admin")
                    .password("admin")
                    .authorities(List.of(roleAdmin))
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        };
    }
}
