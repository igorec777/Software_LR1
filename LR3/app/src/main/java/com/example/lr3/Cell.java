package com.example.lr3;

public class Cell
{
    private String id;
    private String playerId;
    private int number;
    private int image;
    private int state;

    public Cell()
    {

    }

    public Cell(String id, String playerId, int number, int image, int state)
    {
        this.id = id;
        this.playerId = playerId;
        this.number = number;
        this.image = image;
        this.state = state;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getImage()
    {
        return image;
    }

    public void setImage(int image)
    {
        this.image = image;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public String getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }
}
