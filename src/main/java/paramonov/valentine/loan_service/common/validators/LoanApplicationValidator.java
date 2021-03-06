package paramonov.valentine.loan_service.common.validators;

import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;

public interface LoanApplicationValidator {
    void validateLoanApplication(LoanApplicationVo applicationDetails);

    void validateUserName(String username);

    void validatePassword(String password);
}
