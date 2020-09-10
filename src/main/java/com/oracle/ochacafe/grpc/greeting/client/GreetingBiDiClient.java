package com.oracle.ochacafe.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingBiDiClient {
    public static void main(String[] args) {
        System.out.println("Hello gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()//開発用
                .build();

        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        System.out.println("stub created");

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetBiDiStreamRequest> requestStreamObserver = asyncClient.greetBiDiStream(new StreamObserver<GreetBiDiStreamResponse>() {
            @Override
            public void onNext(GreetBiDiStreamResponse value) {
                System.out.println("response: " + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Server completed to send data");
                latch.countDown();
            }
        });

        Arrays.asList("Jordan", "Bobby", "Mo", "Raheem", "Sadio", "Jurgen").forEach(
                name -> {
                    System.out.println("sending: " + name);
                    requestStreamObserver.onNext(GreetBiDiStreamRequest.newBuilder()
                            .setGreeting(Greeting.newBuilder()
                                    .setFirstName(name)
                                    .build())
                            .build());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        );

        requestStreamObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("shutdown");
        channel.shutdown();

    }
}
