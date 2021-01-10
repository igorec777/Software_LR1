package com.example.lr3;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.lr3.Adapters.CustomAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity
{
    private EditText userNickname;
    private ImageView userPhoto;
    private Uri photoUri;
    private Button createOrDeleteRoomBtn;

    private StorageTask uploadTask;

    private CustomAdapter customAdapter;
    private ListView lv;
    private ArrayList<Room> roomList;

    private FirebaseUser user;
    private DatabaseReference dbRoomRef;
    private StorageReference mStorageRef;

    private boolean isRoomExist = false;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Профиль");

        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference(user.getUid());
        dbRoomRef = FirebaseDatabase.getInstance().getReference("Room");

        roomList = new ArrayList<>();

        createOrDeleteRoomBtn = findViewById(R.id.createOrDeleteRoomBtn);
        userNickname = findViewById(R.id.userNickname);
        userPhoto = findViewById(R.id.userPhoto);
        lv = (ListView)findViewById(R.id.roomList);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id)
            {
                Room room;
                room = (Room) lv.getItemAtPosition(position);
                startGame(room);
            }
        });

        userNickname.setText(user.getDisplayName());

        Glide.with(this).load(user.getPhotoUrl()).
                into(userPhoto);

        readRooms();

        String ownerId = user.getUid();
    }

    public void logoutClick(View view)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
    }

    public void changeNickname(View view)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle("Имя");
        alertDialog.setMessage("Введите новое имя");

        final EditText input = new EditText(ProfileActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        alertDialog.setView(input);

        alertDialog.setPositiveButton("Да",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        makeChanges(input.getText().toString(), null);
                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton("Нет",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void makeChanges(String nickName, Uri photo)
    {
        UserProfileChangeRequest profileUpdates;

        if (nickName != null)
        {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nickName)
                    .build();
        }
        else if (photo != null)
        {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(photo)
                    .build();
        }
        else
            return;

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Данные обновлены!",
                                    Toast.LENGTH_LONG).show();
                            userNickname.setText(user.getDisplayName());
                            userPhoto.setImageURI(photoUri);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Ошибка!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void changePhoto(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            photoUri = data.getData();

            makeChanges(null, photoUri);

            if (uploadTask != null && uploadTask.isInProgress())
            {
            }
            else
                uploadInStorage(photoUri);
        }
    }

    private void uploadInStorage(Uri photoUri)
    {
        uploadTask = mStorageRef.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {
                    }
                });
    }

    public void createOrDeleteRoom(View view)
    {
        DatabaseReference roomRef;
        Room room;
        String id;
        String ownerId = user.getUid();

        if (isRoomExist)
        {
            dbRoomRef.child(roomId).removeValue();

            Toast.makeText(getApplicationContext(), "Комната удалена!", Toast.LENGTH_LONG)
                    .show();
            isRoomExist = false;
        }
        else
        {
            roomRef = FirebaseDatabase.getInstance().getReference("Room").push();

            id = roomRef.getKey();

            room = new Room(id, ownerId, null);
            roomRef.setValue(room);
            Toast.makeText(getApplicationContext(), "Комната добавлена!", Toast.LENGTH_LONG)
                            .show();
        }
    }

    public void readRooms()
    {
        ValueEventListener vListener = new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                roomList.clear();

                for (DataSnapshot ds: snapshot.getChildren())
                {
                    Room room = ds.getValue(Room.class);
                    assert room != null;
                    roomList.add(room);

                    if (room.getOwnerId().equals(user.getUid()))
                    {
                        isRoomExist = true;
                        roomId = ds.getKey();

                        if (room.getOpponentId() != null)
                        {
                            Intent intent = new Intent(ProfileActivity.this, GameActivity.class);
                            intent.putExtra("role", 1);
                            intent.putExtra("roomId", roomId);
                            startActivity(intent);
                        }
                    }
                }

                customAdapter.notifyDataSetChanged();

                if (isRoomExist)
                    createOrDeleteRoomBtn.setText("Удалить комнату");
                else
                    createOrDeleteRoomBtn.setText("Создать комнату");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        };

        dbRoomRef.addValueEventListener(vListener);

        customAdapter = new CustomAdapter(this, roomList);
        lv.setAdapter(customAdapter);
    }

    private void startGame(Room room)
    {
        if (user.getUid().equals(room.getOwnerId()))
        {
                Toast.makeText(getApplicationContext(), "Ожидание соперника", Toast.LENGTH_LONG)
                        .show();
        }
        else
        {
            dbRoomRef.child(room.getId()).child("opponentId").setValue(user.getUid());
            Intent intent = new Intent(ProfileActivity.this, GameActivity.class);
            intent.putExtra("role", 0);
            intent.putExtra("roomId", roomId);
            startActivity(intent);
        }
    }

    public void createGravatar(View view)
    {
        String email = user.getEmail();
        String hash = MD5Util.md5Hex(email);
        Uri gravatarUri = Uri.parse("https://www.gravatar.com/avatar/" + hash);
        makeChanges(null, gravatarUri);

    }

    public void openStats(View view)
    {
        Intent intent = new Intent(ProfileActivity.this, StatsActivity.class);
        startActivity(intent);
    }
}
