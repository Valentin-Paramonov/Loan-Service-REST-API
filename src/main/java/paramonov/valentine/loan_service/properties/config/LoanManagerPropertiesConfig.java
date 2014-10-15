package paramonov.valentine.loan_service.properties.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import paramonov.valentine.loan_service.properties.LoanManagerProperties;
import paramonov.valentine.loan_service.util.Time;

import java.math.BigDecimal;

@Configuration
class LoanManagerPropertiesConfig {
    @Value("${loan.manager.maxTermDays}")
    private Integer maxTermDays;

    @Value("${loan.manager.maxApplicationsPerDay}")
    private Integer maxApplicationsPerDay;

    @Value("${loan.manager.maxAmount}")
    private BigDecimal maxAmount;

    @Value("${loan.manager.riskyTimeFrom}")
    private Time riskyTimeFrom;

    @Value("${loan.manager.riskyTimeTill}")
    private Time riskyTimeTill;

    @Bean
    public LoanManagerProperties loanManagerProperties() {
        return new LoanManagerProperties() {
            @Override
            public Time getRiskyTimeFrom() {
                return riskyTimeFrom;
            }

            @Override
            public Time getRiskyTimeTill() {
                return riskyTimeTill;
            }

            @Override
            public Integer getMaxApplicationsPerDay() {
                return maxApplicationsPerDay;
            }

            @Override
            public BigDecimal getMaxAmount() {
                return maxAmount;
            }

            @Override
            public Integer getMaxTermDays() {
                return maxTermDays;
            }
        };
    }
}