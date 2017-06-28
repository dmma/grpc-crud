package ua.dp.dmma.crud.client;

/**
 * @author dmma
 */
public enum Operation
{
    CREATE("c", "Creates dto with given params"), UPDATE("u", "Updates dto"), DELETE("d", "Deletes dto by its i field"), LIST("l",
                "Returns list of saved dto"), HELP("h", ""), EXIT("e", "Exit from client");

    private String operationCode;
    private String description;

    Operation(String operationCode, String description)
    {
        this.operationCode = operationCode;
        this.description = description;
    }

    public static Operation getOperationByCode(String operationCode)
    {
        for (Operation operation : values())
        {
            if (operation.operationCode.equals(operationCode))
            {
                return operation;
            }
        }
        throw new IllegalArgumentException("\nUnknown operationCode : " + operationCode + "\nEnter h for operation list");
    }

    public String getOperationCode()
    {
        return operationCode;
    }

    public String getDescription()
    {
        return description;
    }
}
