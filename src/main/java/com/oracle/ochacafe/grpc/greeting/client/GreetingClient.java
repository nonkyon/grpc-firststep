package com.oracle.ochacafe.grpc.greeting.client;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("Hello gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()//開発用
                .build();

        GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);
        System.out.println("stub created");

        Greeting greeting = Greeting.newBuilder()
                .setFirstName("ocha")
                .setLastName("cafe")
                .build();

        GreetRequest greetRequest = GreetRequest.newBuilder()
                .setGreeting(greeting)
                .build();

        GreetResponse greetResponse = greetClient.greet(greetRequest);
        System.out.println(greetResponse.getResult());

        System.out.println("shutdown");
        channel.shutdown();

    }
}
