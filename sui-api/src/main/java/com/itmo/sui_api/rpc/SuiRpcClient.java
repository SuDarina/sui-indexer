package com.itmo.sui_api.rpc;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

// ref page - https://docs.sui.io/sui-api-ref#suix_getbalance
public class SuiRpcClient {
    private static final String SUI_RPC_URL = "https://fullnode.devnet.sui.io";

    public static void main(String[] args) {
        WebClient webClient = WebClient.create(SUI_RPC_URL);

//      suix_getBalance
        String response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "jsonrpc": "2.0",
                            "id": 1,
                            "method": "suix_getBalance",
                            "params": [
                              "0x51ceab2edc89f74730e683ebee65578cb3bc9237ba6fca019438a9737cf156ae",
                              "0x168da5bf1f48dafc111b0a488fa454aca95e0b5e::usdc::USDC"
                            ]
                          }
                        """)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Ответ от Sui: " + response);
    }
}
