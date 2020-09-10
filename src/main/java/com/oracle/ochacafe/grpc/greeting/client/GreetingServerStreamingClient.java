package com.oracle.ochacafe.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingServerStreamingClient {
    public static void main(String[] args) {
        System.out.println("Hello gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()//開発用
                .build();

        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        System.out.println("stub created");

        GreetServerStreamRequest request = GreetServerStreamRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("ocha")
                        .setLastName("cafe")
                        .build())
                .build();
        greetClient.greetServerStream(request).forEachRemaining(greetServerStreamResponse -> {
            System.out.println(greetServerStreamResponse.getResult());
        });

        System.out.println("shutdown");
        channel.shutdown();

    }
}
