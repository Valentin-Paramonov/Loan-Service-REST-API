package paramonov.valentine.loan_service.web.managers;

import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;

public interface LoanManager {
    void applyForLoan(LoanApplicationVo application);
}
