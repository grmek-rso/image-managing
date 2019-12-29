package com.grmek.rso.imagemanaging;

import com.kumuluz.ee.grpc.client.GrpcChannelConfig;
import com.kumuluz.ee.grpc.client.GrpcChannels;
import com.kumuluz.ee.grpc.client.GrpcClient;
import io.grpc.stub.StreamObserver;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLException;

@ApplicationScoped
public class SharingGrpcService {

    private SharingGrpc.SharingStub stub;

    @PostConstruct
    public void init() {
        try {
            GrpcChannels clientPool = GrpcChannels.getInstance();
            GrpcChannelConfig config = clientPool.getGrpcClientConfig("client1");
            GrpcClient client = new GrpcClient(config);
            stub = SharingGrpc.newStub(client.getChannel());
        } catch (SSLException e) {
            System.err.println(e);
        }
    }

    public void userCleanUp(Integer id) {
        SharingService.CleanUpRequest request = SharingService.CleanUpRequest.newBuilder().setId(id).build();

        stub.userCleanUp(request, new StreamObserver<SharingService.CleanUpResponse>() {
            @Override
            public void onNext(SharingService.CleanUpResponse response) {
                if (response.getStatus() != 0) {
                    System.err.println("User clean up error");
                }
            }

            @Override
            public void onError(Throwable e) {
                System.err.println(e.getMessage());
            }

            @Override
            public void onCompleted() {
            }
        });
    }

    public void albumCleanUp(Integer id) {
        SharingService.CleanUpRequest request = SharingService.CleanUpRequest.newBuilder().setId(id).build();

        stub.albumCleanUp(request, new StreamObserver<SharingService.CleanUpResponse>() {
            @Override
            public void onNext(SharingService.CleanUpResponse response) {
                if (response.getStatus() != 0) {
                    System.err.println("Album clean up error");
                }
            }

            @Override
            public void onError(Throwable e) {
                System.err.println(e);
            }

            @Override
            public void onCompleted() {
            }
        });
    }
}
