package com.oracle.ochacafe.grpc.greeting.client;

import com.proto.greet.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClientStreamingClient {

    public static void main(String[] args) {
        System.out.println("Hello gRPC client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()//開発用
                .build();

        GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
        System.out.println("stub created");

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<GreetClientStreamRequest> requestStreamObserver = asyncClient.greetClientStream(new StreamObserver<GreetClientStreamResponse>() {
            @Override
            public void onNext(GreetClientStreamResponse value) {
                System.out.println("received response: " + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                //DO NOTHING
            }

            @Override
            public void onCompleted() {
                System.out.println("complete streaming");
                latch.countDown();
            }
        });

        System.out.println("Greeting 1");
        requestStreamObserver.onNext(GreetClientStreamRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Jordan")
                        .build())
                .build());

        System.out.println("Greeting 2");
        requestStreamObserver.onNext(GreetClientStreamRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Bobby")
                        .build())
                .build());

        System.out.println("Greeting 3");
        requestStreamObserver.onNext(GreetClientStreamRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Mo")
                        .build())
                .build());

        System.out.println("Greeting 4");
        requestStreamObserver.onNext(GreetClientStreamRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Sadio")
                        .build())
                .build());

        System.out.println("Greeting 5");
        requestStreamObserver.onNext(GreetClientStreamRequest.newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Jurgen")
                        .build())
                .build());

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
