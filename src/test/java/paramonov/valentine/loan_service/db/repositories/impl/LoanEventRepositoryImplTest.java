package paramonov.valentine.loan_service.db.repositories.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import paramonov.valentine.loan_service.TestDatabaseConfig;
import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.User;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, RepositoryTestConfig.class})
@TransactionConfiguration(defaultRollback = false)
public class LoanEventRepositoryImplTest {
    private static final String IP_ADDRESS = "ip";
    private static final LoanEventStatus STATUS = LoanEventStatus.APPLICATION;
    private static boolean populated;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private LoanEventRepositoryImpl loanEventRepositoryImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private User user;
    private LoanApplication loanApplication;
    private Date date;
    private LoanApplicationVo loanApplicationVo;

    @Before
    @Transactional
    public void setUp() {
        setInstanceVariables();
        if(populated) {
            return;
        }
        populated = true;
        populateDatabase();
    }

    @Test
    public void testNewEvent_CompareSavedObjectIpWithLoadedOne_ShouldBeEqual() {
        final String loadedIp = jdbcTemplate.queryForObject("select applicant_ip from event", String.class);

        assertThat(loadedIp, equalTo(IP_ADDRESS));
    }

    @Test
    public void testNewEvent_CompareEventUserIdWithSavedUserId_ShouldBeEqual() {
        final String userIdQuery = String.format("select id from user where name='%s'", user.getName());
        final Long userId = jdbcTemplate.queryForObject(userIdQuery, Long.class);
        final Long eventUserID = jdbcTemplate.queryForObject("select user from event", Long.class);

        assertThat(eventUserID, equalTo(userId));
    }

    @Test
    public void testNewEvent_CompareSavedObjectStatusWithLoadedOne_ShouldBeEqual() {
        final String loadedStatusString = jdbcTemplate.queryForObject("select status from event", String.class);
        final LoanEventStatus loadedStatus = LoanEventStatus.valueOf(loadedStatusString);

        assertThat(loadedStatus, equalTo(STATUS));
    }

    @Test
    @Transactional(readOnly = true)
    public void testGetNumberOfApplicationsInLast24Hours() {
        final int numberOfApplications = loanEventRepositoryImpl.getNumberOfApplicationsInLast24Hours(IP_ADDRESS);

        assertThat(numberOfApplications, equalTo(1));
    }

    private void populateDatabase() {
        final Session session = sessionFactory.getCurrentSession();

        session.save(user);
        session.save(loanApplication);
        loanEventRepositoryImpl.newEvent(loanApplicationVo, loanApplication, STATUS);
    }

    private void setInstanceVariables() {
        date = new Date();
        user = new User()
            .setName("John")
            .setPassword("wwww");
        loanApplication = new LoanApplication()
            .setLoanAmount(BigDecimal.TEN)
            .setLoanInterest(BigDecimal.ONE)
            .setUser(user)
            .setDueDate(date);
        setupMocks();
    }

    private void setupMocks() {
        loanApplicationVo = Mockito.mock(LoanApplicationVo.class);
        Mockito.when(loanApplicationVo.getApplicantIp()).thenReturn(IP_ADDRESS);
        Mockito.when(loanApplicationVo.getApplicant()).thenReturn(user);
    }
}
