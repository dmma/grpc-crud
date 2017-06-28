package ua.dp.dmma.crud.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ua.dp.dmma.crud.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ua.dp.dmma.crud.client.Operation.HELP;

/**
 * @author dmma
 */
public class CrudConsoleClient
{
    private static final Logger LOG = Logger.getLogger(CrudConsoleClient.class.getName());

    private ManagedChannel channel;
    private CrudServiceGrpc.CrudServiceBlockingStub stub;

    private void initializeStub()
    {
        channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext(true).build();
        stub = CrudServiceGrpc.newBlockingStub(channel);
    }

    private void shutdown() throws InterruptedException
    {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private DtoList getDtoList()
    {
        return stub.getDtoList(Empty.getDefaultInstance());
    }

    private ProcessingResponse createDto(int i, String s, boolean b)
    {
        return stub.createDto(Dto.newBuilder().setI(i).setS(s).setB(b).build());
    }

    private ProcessingResponse updateDto(int i, String s, boolean b)
    {
        return stub.updateDto(Dto.newBuilder().setI(i).setS(s).setB(b).build());
    }

    private ProcessingResponse deleteDto(int i)
    {
        return stub.deleteDto(Dto.newBuilder().setI(i).build());
    }

    public static void main(String[] args)
    {
        CrudConsoleClient client = new CrudConsoleClient();
        client.initializeStub();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in)))
        {
            String helpingMessage = createHelpMessage();
            while (true)
            {
                LOG.info("\nEnter command");
                String input = bufferedReader.readLine();
                if (input != null && !input.trim().isEmpty())
                {
                    StringTokenizer stringTokenizer = new StringTokenizer(input, " \t");
                    String command = stringTokenizer.nextToken();
                    try
                    {
                        Operation operation = Operation.getOperationByCode(command);
                        switch (operation)
                        {
                            case LIST:
                                DtoList dtoList = client.getDtoList();
                                LOG.info("\nStorage status :\n" + (dtoList.getDtoList().isEmpty() ? "EMPTY" : dtoList.toString()));
                                break;
                            case CREATE:
                                if (stringTokenizer.countTokens() == 3)
                                    client.createDto(Integer.valueOf(stringTokenizer.nextToken()), stringTokenizer.nextToken(),
                                                    Boolean.valueOf(stringTokenizer.nextToken()));
                                else
                                    LOG.warning("Invalid params count for create operation");
                                break;
                            case UPDATE:
                                if (stringTokenizer.countTokens() == 3)
                                    client.updateDto(Integer.valueOf(stringTokenizer.nextToken()), stringTokenizer.nextToken(),
                                                    Boolean.valueOf(stringTokenizer.nextToken()));
                                else
                                    LOG.warning("Invalid params count for update operation");
                                break;
                            case DELETE:
                                if (stringTokenizer.countTokens() == 1)
                                    client.deleteDto(Integer.valueOf(stringTokenizer.nextToken()));
                                else
                                    LOG.warning("Invalid params count for delete operation");
                                break;
                            case HELP:
                                LOG.info(helpingMessage);
                                break;
                            case EXIT:
                                try
                                {
                                    client.shutdown();
                                }
                                catch (InterruptedException e)
                                {
                                    LOG.warning("Error during client shutting down:" + e.getMessage());
                                }
                                finally
                                {
                                    System.exit(0);
                                }
                        }
                    }
                    catch (IllegalArgumentException e)
                    {
                        LOG.warning(e.getMessage());
                    }
                }
                else
                {
                    LOG.warning("Input command can't be null or empty");
                }
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, "Console client error", e.getMessage());
        }
    }

    private static String createHelpMessage()
    {
        List<String> list = Arrays.stream(Operation.values()).filter(o -> o != HELP).map(o -> o.getOperationCode() + " " + o.getDescription())
                        .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append("Commands list : ").append("\n");
        list.forEach(s -> {
            sb.append(s);
            sb.append("\n");
        });
        return sb.toString();
    }
}


