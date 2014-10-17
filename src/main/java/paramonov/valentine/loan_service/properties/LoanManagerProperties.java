package paramonov.valentine.loan_service.properties;

import paramonov.valentine.loan_service.util.Time;

import java.math.BigDecimal;

public interface LoanManagerProperties {
    Time getRiskyTimeFrom();

    Time getRiskyTimeTill();

    Integer getMaxApplicationsPerDay();

    BigDecimal getMinAmount();

    BigDecimal getMaxAmount();

    BigDecimal getDefaultInterest();

    Integer getMinTermDays();

    Integer getMaxTermDays();
}
