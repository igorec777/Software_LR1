package com.example.lr3;

public class Room
{
    private String id;
    private String ownerId;
    private String opponentId;

    public Room()
    {

    }

    public Room (String id, String ownerId, String opponentId)
    {
        this.id = id;
        this.ownerId = ownerId;
        this.opponentId = opponentId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String uId)
    {
        this.id = uId;
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getOpponentId()
    {
        return opponentId;
    }

    public void setOpponentId(String opponentId)
    {
        this.opponentId = opponentId;
    }
}
