package com.tryingpfq.rpc.client;

import com.tryingpfq.rpc.RpcClient;
import com.tryingpfq.rpc.CalculatorService;

/**
 * @author tryingpfq
 * @date 2020/2/25
 **/
public class ClientApplication {

    public static void main(String[] args) {
        RpcClient client = new RpcClient();
        try {
            CalculatorService refer = client.refer(CalculatorService.class);
            int result = refer.add(1, 2);
            System.err.println("result : " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
