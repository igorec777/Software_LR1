package com.example.lr3;

import java.util.ArrayList;

public class Ship
{
    private String id;
    private String playerId;
    private ArrayList<Integer> positions;
    private ArrayList<Boolean> posStates;

    public Ship()
    {

    }

    public Ship(String id, String playerId, ArrayList<Integer> positions, ArrayList<Boolean> posStates)
    {
        this.id = id;
        this.playerId = playerId;
        this.positions = positions;
        this.posStates = posStates;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPlayerId()
    {
        return playerId;
    }

    public void setPlayerId(String playerId)
    {
        this.playerId = playerId;
    }

    public ArrayList<Integer> getPositions()
    {
        return positions;
    }

    public void setPositions(ArrayList<Integer> positions)
    {
        this.positions = positions;
    }

    public ArrayList<Boolean> getPosStates()
    {
        return posStates;
    }

    public void setPosStates(ArrayList<Boolean> posStates)
    {
        this.posStates = posStates;
    }
}
