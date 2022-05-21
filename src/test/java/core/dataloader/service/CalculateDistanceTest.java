package core.dataloader.service;

import org.assertj.core.internal.bytebuddy.implementation.bind.annotation.Argument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.*;

class CalculateDistanceTest {

    private CalculateDistance calculateDistance;

    @BeforeEach
    void setup() {
        calculateDistance = new CalculateDistance();
    }

    private static Stream<Arguments> coordsToTime() {
        return Stream.of(
                arguments("477|626", 17819),
                arguments("526|434", 431544),
                arguments("474|632", 6300),
                arguments("414|632", 119700),
                arguments("410|583", 164311),
                arguments("581|355", 625888),
                arguments("472|359", 573304),
                arguments("597|633", 264608),
                arguments("472|632", 2100),
                arguments("490|586", 104516)
        );
    }

    @ParameterizedTest
    @MethodSource("coordsToTime")
    void coords(String targetCoords, Integer result) {
        String attackerCords = "471|632";
        assertThat(calculateDistance.calculate(targetCoords, attackerCords, 35)).isEqualTo(result);
    }
}