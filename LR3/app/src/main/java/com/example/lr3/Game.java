package com.example.lr3;

public class Game
{
    private String id;
    private int currentTurn;


    public Game()
    {

    }

    public Game(String id, int currentTurn)
    {
        this.id = id;
        this.currentTurn = currentTurn;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getCurrentTurn()
    {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn)
    {
        this.currentTurn = currentTurn;
    }
}
