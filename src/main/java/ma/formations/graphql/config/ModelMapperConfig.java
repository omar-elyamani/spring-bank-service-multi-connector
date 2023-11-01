package ma.formations.graphql.config;

import lombok.AllArgsConstructor;
import ma.formations.graphql.common.CommonTools;
import ma.formations.graphql.dtos.transaction.TransactionDto;
import ma.formations.graphql.enums.TransactionType;
import ma.formations.graphql.service.model.BankAccountTransaction;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@AllArgsConstructor
public class ModelMapperConfig {
    private CommonTools tools;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Converter<Date, String> dateConverter = new AbstractConverter<>() {
            @Override
            protected String convert(Date date) {
                return tools.dateToString(date);
            }
        };

        Converter<TransactionType, String> transactionTypeConverter = new AbstractConverter<>() {
            @Override
            protected String convert(TransactionType transactionType) {
                return transactionType.name();
            }
        };

        modelMapper.addConverter(dateConverter);
        modelMapper.addConverter(transactionTypeConverter);
        modelMapper.createTypeMap(TransactionDto.class, BankAccountTransaction.class);
        return modelMapper;
    }
}
