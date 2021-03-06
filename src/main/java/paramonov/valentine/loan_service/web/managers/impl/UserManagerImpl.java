package paramonov.valentine.loan_service.web.managers.impl;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.validators.LoanApplicationValidator;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.UserRepository;
import paramonov.valentine.loan_service.web.managers.UserManager;
import paramonov.valentine.loan_service.web.managers.exceptions.UserNameAlreadyExistsException;

@Component("userManager")
class UserManagerImpl implements UserManager {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringEncryptor encryptor;

    @Autowired
    private LoanApplicationValidator validator;

    @Override
    public void registerUser(String userName, String password) {
        validator.validateUserName(userName);
        validator.validatePassword(password);
        checkUserNameAvailable(userName);

        final String encryptedPassword = encryptor.encrypt(password);

        userRepository.newUser(userName, encryptedPassword);
    }

    void checkUserNameAvailable(String userName) {
        final User user = userRepository.getUserByName(userName);
        if(user != null) {
            throw new UserNameAlreadyExistsException();
        }
    }
}
