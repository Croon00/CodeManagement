package com.kstec.codemangement.init;

import com.kstec.codemangement.model.entity.Code;
import com.kstec.codemangement.model.entity.User;
import com.kstec.codemangement.model.entity.User.Role;
import com.kstec.codemangement.model.repository.UserRepository;
import com.kstec.codemangement.model.repository.CodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class Initializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CodeRepository codeRepository;
    private final PasswordEncoder passwordEncoder;

    public Initializer(UserRepository userRepository, CodeRepository codeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Admin 계정 생성
        User adminUser = userRepository.findByLoginId("admin").orElseGet(() -> {
            User newAdmin = new User();
            newAdmin.setLoginId("admin");
            newAdmin.setPassword(passwordEncoder.encode("Kstec007!")); // 비밀번호 암호화
            newAdmin.setUserName("운영자");
            newAdmin.setActivated(true);
            newAdmin.setRole(Role.ADMIN);
            userRepository.save(newAdmin);
            System.out.println("Admin user created: ID=admin, Password=Kstec007!");
            return newAdmin;
        });

        // 무작위 Code 데이터 생성 (20개)
        Random random = new Random();
        List<Code> createdCodes = new ArrayList<>();

        IntStream.range(0, 20).forEach(i -> {
            Code code = new Code();
            code.setCodeName("CodeName" + i);
            code.setCodeValue("Value" + random.nextInt(1000));
            code.setCodeMean("This is a generated code " + i);
            code.setActivated(random.nextBoolean());
            code.setUser(adminUser); // 운영자가 생성한 코드로 설정
            Code savedCode = codeRepository.save(code);

            // 저장된 코드 중 일부를 부모 코드로 사용하기 위해 리스트에 추가
            createdCodes.add(savedCode);
        });

        // 생성된 코드 중 5개의 코드에 부모 코드 설정
        IntStream.range(0, 5).forEach(i -> {
            Code childCode = new Code();
            childCode.setCodeName("ChildCodeName" + i);
            childCode.setCodeValue("ChildValue" + random.nextInt(1000));
            childCode.setCodeMean("This is a child code " + i);
            childCode.setActivated(true);
            childCode.setUser(adminUser);

            // 무작위로 부모 코드 설정
            Code parentCode = createdCodes.get(random.nextInt(createdCodes.size()));
            childCode.setParentCodeId(parentCode);

            codeRepository.save(childCode);
        });

        System.out.println("20 random Code entries created by admin user.");
        System.out.println("5 child Code entries with parent codes created by admin user.");
    }
}
