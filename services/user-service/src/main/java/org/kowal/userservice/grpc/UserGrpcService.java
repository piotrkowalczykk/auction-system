package org.kowal.userservice.grpc;

import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.kowal.user.grpc.GetUserProfileRequest;
import org.kowal.user.grpc.GetUserProfileResponse;
import org.kowal.user.grpc.UserServiceGrpc;
import org.kowal.userservice.exception.mapper.GrpcExceptionMapper;
import org.kowal.userservice.service.UserProfileService;

@GrpcService
@AllArgsConstructor
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
    private final UserProfileService userProfileService;

    @Override
    public void getUserProfile(GetUserProfileRequest request, StreamObserver<GetUserProfileResponse> responseObserver) {
        try {
            GetUserProfileResponse response = userProfileService.getUserProfile(request.getUserId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            System.out.println("Error in getUserProfile: " + ex.getMessage());
            responseObserver.onError(
                    GrpcExceptionMapper
                            .map(ex)
                            .asRuntimeException()
            );
        }
    }
}
