package com.example.lr3;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Random;

public class ShipsCreator
{
    private ArrayList<Rect> ships;

    private int[] leftCount;

    public ShipsCreator()
    {
        ships = new ArrayList<Rect>();
        leftCount = new int[] {4,3,2,1};
    }

    public ArrayList<Rect> getShips()
    {
        return ships;
    }

    public int getLeftCount(int i)
    {
        return leftCount[i - 1];
    }

    public void generateShips()
    {
        Random r = new Random();
        for (int i = 0; i < 4; i++)
        {
            while (leftCount[i] > 0)
            {
                while (true)
                {
                    int x = r.nextInt(10);
                    int y = r.nextInt(10);
                    int dir = r.nextInt(2);

                    Rect ship = new Rect(
                            x, y,
                            x + (dir == 0 ? i : 0),
                            y + (dir == 1 ? i : 0)
                    );

                    if (ship.right >= 10 || ship.bottom >= 10)
                    {
                        continue;
                    }

                    boolean ok = true;
                    for (int j = 0; j < ships.size(); j++)
                    {
                        if (touch(ships.get(j), ship))
                        {
                            ok = false;
                            break;
                        }
                    }

                    if (ok)
                    {
                        ships.add(ship);
                        break;
                    }
                }
                leftCount[i] -= 1;
            }
        }
    }

    private boolean touch(Rect s1, Rect s2)
    {
        return s2.intersects(s1.left - 2, s1.top - 2, s1.right + 2, s1.bottom + 2);
    }
}
