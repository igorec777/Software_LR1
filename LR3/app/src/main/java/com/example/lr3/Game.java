package com.example.lr3;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Game
{
    public enum CellState
    {
        EMPTY,
        ENTRY,
        MISS,
        SHIP
    }

    public enum GameState
    {
        FIRST_MOVE,
        SECOND_MOVE
    }

    private GameState gameState;
    private List<List<CellState>> field1, field2;
    private boolean finished;

    public Game()
    {
        gameState = GameState.FIRST_MOVE;
        finished = false;

        field1 = new ArrayList<>();
        field2 = new ArrayList<>();

        for (int i = 0; i < 10; i++)
        {
            ArrayList<CellState> a = new ArrayList<>();
            ArrayList<CellState> b = new ArrayList<>();

            for (int j = 0; j < 10; j++)
            {
                a.add(CellState.EMPTY);
                b.add(CellState.EMPTY);
            }

            field1.add(a);
            field2.add(b);
        }
    }

    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public List<List<CellState>> getField1()
    {
        return field1;
    }

    public List<List<CellState>> getField2()
    {
        return field2;
    }

    public boolean makeMove(GameState role, int x, int y)
    {
        if (role == GameState.FIRST_MOVE)
        {
            if (field2.get(x).get(y) == CellState.ENTRY || field2.get(x).get(y) == CellState.MISS)
            {
                return false;
            }
            if (field2.get(x).get(y) == CellState.EMPTY)
            {
                gameState = GameState.SECOND_MOVE;
                field2.get(x).set(y, CellState.MISS);
            }
            else
            {
                field2.get(x).set(y, CellState.ENTRY);
            }

        }
        else {
            if (field1.get(x).get(y) == CellState.ENTRY || field1.get(x).get(y) == CellState.MISS)
            {
                return false;
            }
            if (field1.get(x).get(y) == CellState.EMPTY)
            {
                gameState = GameState.FIRST_MOVE;
                field1.get(x).set(y, CellState.MISS);
            }
            else {
                field1.get(x).set(y, CellState.ENTRY);
            }
        }
        return true;
    }
}
