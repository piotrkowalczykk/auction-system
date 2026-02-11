package org.kowal.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "grpc.server.port=-1")
class AuthenticationServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
