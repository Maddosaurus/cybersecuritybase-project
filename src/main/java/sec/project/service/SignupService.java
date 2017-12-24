package sec.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.util.List;

@Service
public class SignupService {

    @Autowired
    private SignupRepository signupRepository;

    public List<Signup> list() {
        return signupRepository.findAll();
    }

    @Transactional
    public void doSignup(String name, String address) {
        Signup signup = new Signup();
        signup.setName(name);
        signup.setAddress(address);

        signupRepository.save(signup);

    }
}
