package core.dataloader.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class CalculateDistance {


    public Integer calculate(String target, String attacker, int time) {
        String[] targetCoords = target.split("\\|");
        Integer xTarget = Integer.parseInt(targetCoords[0]);
        Integer yTarget = Integer.parseInt(targetCoords[1]);

        String[] attackerCoords = attacker.split("\\|");
        Integer xAttacker = Integer.parseInt(attackerCoords[0]);
        Integer yAttacker = Integer.parseInt(attackerCoords[1]);

        int xResult = (xTarget - xAttacker) * (xTarget - xAttacker);
        int yResult = (yTarget - yAttacker) * (yTarget - yAttacker);
        int b = xResult + yResult;
        MathContext mathContext = new MathContext(6);
        BigDecimal decimal = new BigDecimal(b);
        decimal = decimal.sqrt(mathContext);

        return convertToSeconds(decimal, time);
    }

    private Integer convertToSeconds(BigDecimal distance, int time) {
        BigDecimal t = new BigDecimal(time);
        BigDecimal d = new BigDecimal(60);
        distance = distance.multiply(d);
        distance = distance.multiply(t);

        return distance.round(new MathContext(6, RoundingMode.HALF_UP)).intValue();
    }
}
