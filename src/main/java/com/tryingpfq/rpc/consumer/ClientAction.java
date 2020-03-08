package com.tryingpfq.rpc.consumer;

import com.tryingpfq.rpc.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class ClientAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAction.class);

    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        try {
            CalculatorService refer = client.refer(CalculatorService.class);
            int result = refer.add(1, 2);
            LOGGER.info("remote back result: {}", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
