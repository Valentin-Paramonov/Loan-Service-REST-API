package paramonov.valentine.loan_service.db.repositories.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.LoanEvent;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.LoanEventRepository;
import paramonov.valentine.loan_service.util.DateUtils;

import java.util.Date;
import java.util.List;

@Repository("loanEventRepository")
class LoanEventRepositoryImpl implements LoanEventRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void newEvent(LoanApplicationVo applicationDetails, LoanApplication application, LoanEventStatus status) {
        final User applicant = applicationDetails.getApplicant();
        final String applicantIp = applicationDetails.getApplicantIp();
        final Date currentDate = new Date();

        final LoanEvent loanEvent = new LoanEvent()
            .setUser(applicant)
            .setApplication(application)
            .setApplicantIp(applicantIp)
            .setEventStatus(status)
            .setEventDate(currentDate);

        final Session session = sessionFactory.getCurrentSession();
        session.save(loanEvent);
    }

    @Override
    public int getNumberOfApplicationsInLast24Hours(String ipAddress) {
        final Date date24HoursAgo = DateUtils.getDate24HoursAgo();
        final Session session = sessionFactory.getCurrentSession();
        final SimpleExpression eqApplicantIp = Restrictions.eq("applicantIp", ipAddress);
        final SimpleExpression geEventDate = Restrictions.ge("eventDate", date24HoursAgo);
        final Projection rowCount = Projections.rowCount();

        final Long numberOfApplications = (Long) session
            .createCriteria(LoanEvent.class)
            .add(eqApplicantIp)
            .add(geEventDate)
            .setProjection(rowCount)
            .uniqueResult();

        return numberOfApplications.intValue();
    }

    @Override
    public List<LoanEvent> getHistoricEventsForUser(User user) {
        final Session session = sessionFactory.getCurrentSession();
        final SimpleExpression eqUser = Restrictions.eq("user", user);
        final SimpleExpression neStatus = Restrictions.ne("eventStatus", LoanEventStatus.DENIED);

        return session
            .createCriteria(LoanEvent.class)
            .add(eqUser)
            .add(neStatus)
            .list();
    }
}
