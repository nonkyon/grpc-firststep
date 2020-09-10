package com.oracle.ochacafe.grpc.greeting.server;

import com.proto.greet.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
    //unary
    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        String lastName = greeting.getLastName();

        String result = "Hello " + firstName + lastName;
        GreetResponse response = GreetResponse.newBuilder()
                .setResult(result)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    //server streaming
    @Override
    public void greetServerStream(GreetServerStreamRequest request, StreamObserver<GreetServerStreamResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();
        String lastName = request.getGreeting().getLastName();
        try {
            for (int i=1; i<=10; i++){
                String result = "Hello " + firstName + lastName + ", response: " + i;
                GreetServerStreamResponse response = GreetServerStreamResponse.newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(response);
                Thread.sleep(1000L);
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            responseObserver.onCompleted();
        }
        responseObserver.onCompleted();
    }

    //client streaming

    @Override
    public StreamObserver<GreetClientStreamRequest> greetClientStream(StreamObserver<GreetClientStreamResponse> responseObserver) {

        StreamObserver<GreetClientStreamRequest> requestStreamObserver = new StreamObserver<GreetClientStreamRequest>() {

            String result = "";

            @Override
            public void onNext(GreetClientStreamRequest value) {
                String firstName = value.getGreeting().getFirstName();
                String lastName = value.getGreeting().getLastName();
                result += "Hello " + firstName + lastName + ", ";
            }

            @Override
            public void onError(Throwable t) {
                //DO NOTHING
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(
                        GreetClientStreamResponse.newBuilder()
                                .setResult(result)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };

        return requestStreamObserver;
    }

    @Override
    public StreamObserver<GreetBiDiStreamRequest> greetBiDiStream(StreamObserver<GreetBiDiStreamResponse> responseObserver) {
        StreamObserver<GreetBiDiStreamRequest> requestStreamObserver = new StreamObserver<GreetBiDiStreamRequest>() {
            @Override
            public void onNext(GreetBiDiStreamRequest value) {
                String name = value.getGreeting().getFirstName();
                if(name.equals("Raheem")) {
                    return;
                }
                String result = "Hello " + name;
                GreetBiDiStreamResponse greetBiDiStreamResponse = GreetBiDiStreamResponse.newBuilder()
                        .setResult(result)
                        .build();
                responseObserver.onNext(greetBiDiStreamResponse);
            }

            @Override
            public void onError(Throwable t) {
                //DO NOTHING
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
        return requestStreamObserver;
    }
}
