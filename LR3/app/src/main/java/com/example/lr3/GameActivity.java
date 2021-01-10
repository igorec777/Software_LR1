package com.example.lr3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lr3.Adapters.GridViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity
{
    private GridView myGrid, opponentGrid;
    private GridViewAdapter myGridAdapter, opponentGridAdapter;

    private TextView turnText;

    ArrayList<Ship> myShipList;
    ArrayList<Ship> opponentShipList;

    ArrayList<String> cellKeys;
    private ArrayList<Cell> myFieldList, opponentFieldList;

    private FirebaseUser user;

    private int role;
    private String roomId;
    private String gameId;

    private DatabaseReference dbShipRef;
    private DatabaseReference dbCellRef;
    private DatabaseReference dbGameRef;
    private DatabaseReference dbStatsRef;
    private DatabaseReference dbRoomRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().setTitle("Игра");

        Bundle extras = getIntent().getExtras();


        role = extras.getInt("role");

        roomId = extras.getString("roomId");

        turnText = findViewById(R.id.turnText);

        dbShipRef = FirebaseDatabase.getInstance().getReference("Ship");

        dbCellRef = FirebaseDatabase.getInstance().getReference("Cell");

        dbGameRef = FirebaseDatabase.getInstance().getReference("Game");

        dbRoomRef = FirebaseDatabase.getInstance().getReference("Room");

        myGrid = findViewById(R.id.myField);

        opponentGrid = findViewById(R.id.opponentField);

        user = FirebaseAuth.getInstance().getCurrentUser();

        myFieldList = new ArrayList<>();
        opponentFieldList = new ArrayList<>();

        cellKeys = new ArrayList<>();

        myShipList = new ArrayList<>();
        opponentShipList = new ArrayList<>();

        paintCells();
        paintShips();

        try
        {
            generateField();
        }
        catch (Exception ex)
        {
            generateField();
        }

        setGame();
        updateTurn();

        opponentGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cell cell;
                cell = (Cell) opponentGrid.getItemAtPosition(position);

                makeShoot(cell);

                Toast.makeText(GameActivity.this, "Выбрано:" + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setGame()
    {
        DatabaseReference gameRef;

        if (role == 0)
        {
            gameRef = FirebaseDatabase.getInstance().getReference("Game").push();
            gameId = gameRef.getKey();
            gameRef.setValue(new Game(gameId, 0));
        }
    }

    public int getRandom(ArrayList<Integer> types)
    {
        int type;
        int index = new Random().nextInt(types.size());

        type = types.get(index);
        types.remove(index);

        return type;
    }

    public boolean isFreeCell(int pos, ArrayList<Boolean> cellStates)
    {
        int[] chkPos = {pos - 11, pos - 10, pos - 9, pos - 1, pos,
                pos + 1, pos + 9, pos + 10, pos + 11};

        for (int i = 0; i < 9; i++)
        {
            if (chkPos[i] >= 0)
                if (cellStates.get(chkPos[i]))
                    return false;
        }
        return true;
    }

    public ArrayList<Integer> setShip(int type, ArrayList<Boolean> cellStates)
    {
        ArrayList<Integer> shipPositions = new ArrayList<>();

        for (int i = 0; i < 100; i++)
        {
            int j = 0;
            int pos = i;
            boolean status = false;

            while (j < type)
            {
                if (j > 0 && (pos % 10 == 0))
                {
                    shipPositions.clear();
                    break;
                }
                if (isFreeCell(pos, cellStates))
                {
                    shipPositions.add(pos);

                    j++;
                    pos++;

                    if (j == type)
                        status = true;
                }
                else
                {
                    shipPositions.clear();
                    break;
                }
            }

            if (status)
            {
                for (int elem: shipPositions)
                {
                    cellStates.set(elem, true);
                }
                return shipPositions;
            }

        }

        return shipPositions;
    }

    public void generateField()
    {
        DatabaseReference shipRef;
        DatabaseReference cellRef;

        String cellId;
        String shipId;

        int type;
        ArrayList<Integer> currShipPositions;

        ArrayList<Boolean> cellStates = new ArrayList<>();

        for (int i = 0; i < 100; i++)
            cellStates.add(false);

        ArrayList<Integer> types = new ArrayList<Integer>()
        {{
            add(1); add(1); add(1); add(1);
            add(2); add(2); add(2);
            add(3); add(3);
            add(4);
        }};

        int currCellNum = 0;

        for (int i = 0; i < 10; i++)
        {
            type = getRandom(types);

            ArrayList<Boolean> states = new ArrayList<>();

            for (int f = 0; f < type; f++)
                states.add(true);

            currShipPositions = setShip(type, cellStates);

            shipRef = FirebaseDatabase.getInstance().getReference("Ship").push();
            shipId = shipRef.getKey();
            shipRef.setValue(new Ship(shipId, user.getUid(), currShipPositions, states));

            if (currCellNum > 0)
            {
                int diff = currShipPositions.get(0) - currCellNum;

                if (diff > 0)
                {
                    for (int k = 0; k < diff; k++)
                    {
                        cellRef = FirebaseDatabase.getInstance().getReference("Cell").push();
                        cellId = cellRef.getKey();
                        Cell cell = new Cell(cellId, user.getUid(), currCellNum, R.drawable.empty_field, 0);
                        cellRef.setValue(cell);
                        cellKeys.add(cellId);
                        currCellNum++;
                    }
                }
                else
                {
                    for (int j = 0; j < currShipPositions.size(); j++)
                    {
                        dbCellRef.child(cellKeys.get(currShipPositions.get(j)))
                                .child("state").setValue(1);

                        dbCellRef.child(cellKeys.get(currShipPositions.get(j)))
                                .child("image").setValue(R.drawable.ship_field);
                    }
                }
            }

            for(int j = 0; j < currShipPositions.size(); j++)
            {
                cellRef = FirebaseDatabase.getInstance().getReference("Cell").push();
                cellId = cellRef.getKey();
                Cell cell = new Cell(cellId, user.getUid(), currCellNum, R.drawable.ship_field, 1);
                cellRef.setValue(cell);
                cellKeys.add(cellId);
                currCellNum++;
            }

        }

        for (int i = currCellNum; i < 100; i++)
        {
            cellRef = FirebaseDatabase.getInstance().getReference("Cell").push();
            cellId = cellRef.getKey();
            Cell cell = new Cell(cellId, user.getUid(), i, R.drawable.empty_field, 0);
            cellRef.setValue(cell);
            cellKeys.add(cellId);
        }
    }

    public void paintCells()
    {

        ValueEventListener vListener = new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                myFieldList.clear();
                opponentFieldList.clear();

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Cell cell = ds.getValue(Cell.class);

                    if (cell.getPlayerId().equals(user.getUid()))
                        myFieldList.add(cell);
                    else
                    {
                        if (cell.getImage() == R.drawable.ship_field)
                        {
                            Cell cellCopy = cell;
                            cellCopy.setImage(R.drawable.empty_field);
                            opponentFieldList.add(cellCopy);
                        }
                        else
                            opponentFieldList.add(cell);
                    }
                }

                updateShoot(opponentFieldList, opponentShipList);

                if (opponentShipList.size() > 0)
                    if(isGameFinished(opponentShipList))
                {
                    updateStats(true);
                    Toast.makeText(getApplicationContext(), "Победа!", Toast.LENGTH_LONG)
                            .show();
                }

                if (myShipList.size() > 0)
                    if (isGameFinished(myShipList))
                    {
                        updateStats(false);
                        Toast.makeText(getApplicationContext(), "Поражение!", Toast.LENGTH_LONG)
                                .show();
                    }

                myGridAdapter.notifyDataSetChanged();
                opponentGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };

        dbCellRef.addValueEventListener(vListener);

        myGridAdapter = new GridViewAdapter(this, myFieldList);
        myGrid.setAdapter(myGridAdapter);

        opponentGridAdapter = new GridViewAdapter(this, opponentFieldList);
        opponentGrid.setAdapter(opponentGridAdapter);
    }

    public void paintShips()
    {
        ValueEventListener vListener = new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                myShipList.clear();
                opponentShipList.clear();

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Ship ship = ds.getValue(Ship.class);

                    if (ship.getPlayerId().equals(user.getUid()))
                        myShipList.add(ship);
                    else
                        opponentShipList.add(ship);

                }

                myGridAdapter.notifyDataSetChanged();
                opponentGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };

        dbShipRef.addValueEventListener(vListener);
    }


    public void updateShoot(ArrayList<Cell> opponentFieldList, ArrayList<Ship> opponentShipList)
    {
        for (Cell cl: opponentFieldList)
        {
            if (cl.getState() == 4)
            {
                for (Ship sh: opponentShipList)
                {
                    for (int i = 0; i < sh.getPositions().size(); i++)
                    {
                        if(sh.getPositions().get(i) == cl.getNumber())
                        {
                            dbShipRef.child(sh.getId()).child("posStates")
                                    .child(String.valueOf(i)).setValue(false);

                            killShip(sh.getPosStates(), sh.getPositions());

                            dbCellRef.child(cl.getId()).
                                    child("state").setValue(3);
                            dbCellRef.child(cl.getId())
                                    .child("image").setValue(R.drawable.hit_foreground);

                            if (role == 0)
                                dbGameRef.child(gameId).child("currentTurn").setValue(0);
                            else
                                dbGameRef.child(gameId).child("currentTurn").setValue(1);

                            return;
                        }
                    }

                    dbCellRef.child(cl.getId()).child("state").setValue(2);
                    dbCellRef.child(cl.getId()).child("image").setValue(R.drawable.miss_foreground);

                    if (role == 0)
                        dbGameRef.child(gameId).child("currentTurn").setValue(1);
                    else
                        dbGameRef.child(gameId).child("currentTurn").setValue(0);
                }
            }
        }
    }

    public void makeShoot(Cell cell)
    {
        if (opponentFieldList.get(cell.getNumber()).getState() < 2)
        {
            dbCellRef.child(cell.getId()).child("state").setValue(4);
        }

    }

    public void updateTurn()
    {
        ValueEventListener vListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.getChildrenCount() > 0)
                {
                    for (DataSnapshot ds: snapshot.getChildren())
                    {
                        Game game = (Game) ds.getValue(Game.class);

                        gameId = game.getId();

                        if (game.getCurrentTurn() == role)
                        {
                            turnText.setText("Ваш ход");
                            opponentGrid.setEnabled(true);
                        }
                        else
                        {
                            turnText.setText("Ход противника");
                            opponentGrid.setEnabled(false);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };

        dbGameRef.addValueEventListener(vListener);
    }

    public boolean isGameFinished(ArrayList<Ship> shipList)
    {
        for (Ship ship: shipList)
            for (boolean pos: ship.getPosStates())
            {
                if (pos)
                    return false;
            }
        return true;
    }

    public void killShip(ArrayList<Boolean> shipPosStates, ArrayList<Integer> shipPositions)
    {
        ArrayList<int[]> posToDelete = new ArrayList<>();

        int i = 0;
        boolean isShipFall = false;

        while (i < shipPositions.size())
        {
            if (shipPosStates.get(i))
                if (isShipFall)
                {
                    isShipFall = false;
                    break;
                }
                else
                    isShipFall = true;
            i++;
        }

        if (isShipFall)
        {
            for(int pos: shipPositions)
            {
                posToDelete.add(new int[]{pos - 11, pos - 10, pos - 9, pos - 1, pos + 1, pos + 9,
                        pos + 10, pos + 11});
            }
            for(int[] poses: posToDelete)
            {
                for (int pos: poses)
                    if (pos > 0 && !shipPositions.contains(pos))
                    {
                        dbCellRef.child(opponentFieldList.get(pos).getId())
                                .child("state").setValue(2);
                        dbCellRef.child(opponentFieldList.get(pos).getId())
                                .child("image").setValue(R.drawable.miss_foreground);
                    }
            }
        }

    }

    public void updateStats(boolean result)
    {
        String id;

        dbStatsRef = FirebaseDatabase.getInstance().getReference("Stats")
                .child(user.getUid()).child(gameId);

        dbStatsRef.setValue(result);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (roomId != null)
            dbRoomRef.child(roomId).removeValue();
    }

}
