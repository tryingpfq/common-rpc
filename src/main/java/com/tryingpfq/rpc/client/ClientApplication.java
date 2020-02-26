package com.tryingpfq.rpc.client;

import com.tryingpfq.rpc.RpcClient;
import com.tryingpfq.rpc.CalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class ClientApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientApplication.class);

    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        try {
            CalculatorService refer = client.refer(CalculatorService.class);
            int result = refer.add(1, 2);
            LOGGER.info("refer {} ", refer);
            LOGGER.info("result {}" ,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
