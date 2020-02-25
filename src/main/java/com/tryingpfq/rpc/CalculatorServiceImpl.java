package com.tryingpfq.rpc;

import com.tryingpfq.rpc.annotion.Provider;
import org.springframework.stereotype.Component;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
@Provider(clazz = CalculatorServiceImpl.class)
@Component
public class CalculatorServiceImpl implements CalculatorService {

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
