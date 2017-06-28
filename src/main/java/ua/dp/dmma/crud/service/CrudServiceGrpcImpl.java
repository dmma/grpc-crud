package ua.dp.dmma.crud.service;

import io.grpc.stub.StreamObserver;
import ua.dp.dmma.crud.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author dmma
 */
public class CrudServiceGrpcImpl extends CrudServiceGrpc.CrudServiceImplBase
{
    private static final Logger LOG = Logger.getLogger(CrudServiceGrpcImpl.class.getName());

    private List<Dto> dtoList = new ArrayList<>();

    @Override
    public void getDtoList(Empty request, StreamObserver<DtoList> responseObserver)
    {
        responseObserver.onNext(DtoList.newBuilder().addAllDto(dtoList).build());
        responseObserver.onCompleted();
    }

    @Override
    public void createDto(Dto request, StreamObserver<ProcessingResponse> responseObserver)
    {
        LOG.info("Got create dto request:" + request.toString());
        if (!dtoList.contains(request))
        {
            dtoList.add(request);
            responseObserver.onNext(ProcessingResponse.newBuilder().setMessage("OK").build());
            LOG.info("Request processing message:OK");
        }
        else
        {
            responseObserver.onNext(ProcessingResponse.newBuilder().setMessage("ERR").build());
            LOG.info("Request processing message:ERR");
        }

        responseObserver.onCompleted();
    }

    @Override
    public void updateDto(Dto request, StreamObserver<ProcessingResponse> responseObserver)
    {
        Iterator<Dto> iterator = dtoList.iterator();
        while (iterator.hasNext())
        {
            Dto dto = iterator.next();
            if (dto.getI() == request.getI())
                iterator.remove();
        }
        dtoList.add(request);
        responseObserver.onNext(ProcessingResponse.newBuilder().setMessage("OK").build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteDto(Dto request, StreamObserver<ProcessingResponse> responseObserver)
    {
        Iterator<Dto> iterator = dtoList.iterator();
        while (iterator.hasNext())
        {
            Dto dto = iterator.next();
            if (dto.getI() == request.getI())
                iterator.remove();
        }
        responseObserver.onNext(ProcessingResponse.newBuilder().setMessage("OK").build());
        responseObserver.onCompleted();
    }
}
