package com.example.lr3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.List;

public class BoardGridView extends View
{
    private OnMakeMoveHandler onMakeMoveHandler;

    private ShipsCreator creator;
    private Game game;

    private Paint fieldPaint, missPaint, hitPaint, shipPaint, shipPaint2;
    private RectF fieldLeft = new RectF();
    private RectF fieldRight = new RectF();

    private List<Rect> myShips;

    private int width, height;
    private boolean gameInit;
    private Game.GameState role;

    public BoardGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        creator = null;
        role = null;
        myShips = null;

        TypedArray args = getContext().obtainStyledAttributes(attrs, R.styleable.BoardGridView);
        fieldPaint = new Paint();
        fieldPaint.setColor(args.getColor(R.styleable.BoardGridView_fieldColor, 0));
        fieldPaint.setAntiAlias(true);
        fieldPaint.setStyle(Paint.Style.STROKE);
        fieldPaint.setStrokeWidth(5);

        shipPaint = new Paint();
        shipPaint.setColor(getResources().getColor(R.color.colorAccent));
        shipPaint.setAntiAlias(true);
        shipPaint.setStyle(Paint.Style.STROKE);
        shipPaint.setStrokeWidth(10);

        shipPaint2 = new Paint();
        shipPaint2.setColor(Color.argb(15, 255, 59, 48));
        shipPaint2.setAntiAlias(true);
        shipPaint2.setStyle(Paint.Style.FILL);


        hitPaint = new Paint();
        hitPaint.setColor(Color.RED);
        hitPaint.setAntiAlias(true);
        hitPaint.setStyle(Paint.Style.STROKE);
        hitPaint.setStrokeWidth(10);

        missPaint = new Paint();
        missPaint.setColor(Color.BLACK);
        missPaint.setAntiAlias(true);
        missPaint.setStyle(Paint.Style.STROKE);
        hitPaint.setStrokeWidth(7);

        gameInit = args.getBoolean(R.styleable.BoardGridView_gameInitialize, false);

        args.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        float proposedRight = getPaddingLeft() + (width - getPaddingRight() - getPaddingLeft()) * (float)4/9;
        float proposedBottom = height - getPaddingBottom();
        float real = Math.min(proposedRight, proposedBottom);

        fieldLeft.set(getPaddingLeft(), getPaddingTop(), real, real);

        fieldRight.set(width - fieldLeft.width() - getPaddingRight(), getPaddingTop(),
                width - getPaddingRight(), getPaddingTop() + fieldLeft.height());

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        for (int i = 0; i <= 10; i++)
        {
            float verX = (float)i / 10 * fieldLeft.width() + fieldLeft.left;
            canvas.drawLine(verX, fieldLeft.top, verX, fieldLeft.bottom, fieldPaint);

            float horY = (float)i / 10 * fieldLeft.height() + fieldLeft.top;
            canvas.drawLine(fieldLeft.left, horY, fieldLeft.right, horY, fieldPaint);
        }

        if (!gameInit)
        {
            for (int i = 0; i <= 10; i++)
            {
                float verX = (float)i / 10 * fieldRight.width() + fieldRight.left;
                canvas.drawLine(verX, fieldRight.top, verX, fieldRight.bottom, fieldPaint);

                float horY = (float)i / 10 * fieldRight.height() + fieldRight.top;
                canvas.drawLine(fieldRight.left, horY, fieldRight.right, horY, fieldPaint);
            }


            if (role != null && myShips != null)
            {
                for (Rect ship : myShips)
                {
                    drawShip(canvas, fieldLeft, ship.left, ship.top, ship.right, ship.bottom, shipPaint);
                    drawShip(canvas, fieldLeft, ship.left, ship.top, ship.right, ship.bottom, shipPaint2);
                }
            }

            for (int i = 0; i < 10; i++)
            {
                for (int j = 0; j < 10; j++)
                {
                    if (role == Game.GameState.FIRST_MOVE)
                    {
                        switch (game.getField1().get(i).get(j))
                        {
                            case MISS:
                                drawMiss(canvas, fieldLeft, i, j);
                                break;
                            case ENTRY:
                                drawHit(canvas, fieldLeft, i, j);
                                break;
                        }

                        switch (game.getField2().get(i).get(j))
                        {
                            case MISS:
                                drawMiss(canvas, fieldRight, i, j);
                                break;
                            case ENTRY:
                                drawHit(canvas, fieldRight, i, j);
                                break;
                        }
                    }
                    else {
                        switch (game.getField2().get(i).get(j))
                        {
                            case MISS:
                                drawMiss(canvas, fieldLeft, i, j);
                                break;
                            case ENTRY:
                                drawHit(canvas, fieldLeft, i, j);
                                break;
                        }
                        switch (game.getField1().get(i).get(j))
                        {
                            case MISS:
                                drawMiss(canvas, fieldRight, i, j);
                                break;
                            case ENTRY:
                                drawHit(canvas, fieldRight, i, j);
                                break;
                        }
                    }
                }
            }
        }
        else {
            for (int i = 1; i <= 4; i++)
            {
                int countThisShip = 4 - i + 1;
                if (creator != null)
                {
                    countThisShip = creator.getLeftCount(i);
                }

                if (countThisShip != 0)
                {
                    drawShip(canvas, fieldRight, i * 2 - 1, 2, i * 2 - 1, 2 + i - 1, shipPaint);
                    drawShip(canvas, fieldRight, i * 2 - 1, 2, i * 2 - 1, 2 + i - 1, shipPaint2);
                }
                if (creator != null)
                {
                    for (Rect ship : creator.getShips())
                    {
                        drawShip(canvas, fieldLeft, ship.left, ship.top, ship.right, ship.bottom, shipPaint);
                        drawShip(canvas, fieldLeft, ship.left, ship.top, ship.right, ship.bottom, shipPaint2);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        this.getParent().requestDisallowInterceptTouchEvent(true);

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (fieldRight.contains(event.getX(), event.getY()))
                {
                    float unitDistance = fieldRight.width() / 10;

                    float x = event.getX() - fieldRight.left;
                    float y = event.getY() - fieldRight.top;

                    x /= unitDistance;
                    y /= unitDistance;

                    int i = (int)(x);
                    int j = (int)(y);

                    if (game.getGameState() == role)
                    {
                        boolean ok = game.makeMove(role, i, j);

                        if (onMakeMoveHandler != null && ok)
                        {
                            onMakeMoveHandler.onMakeMove(game);
                        }
                        onUpdate();
                    }
                }
                break;
        }
        return true;
    }

    public void setRoleAndShips(int role, List<Rect> myShips)
    {
        this.role = role == 1 ? Game.GameState.FIRST_MOVE : Game.GameState.SECOND_MOVE;
        this.myShips = myShips;
    }

    public void setGame(Game game)
    {
        this.game = game;
        onUpdate();
    }

    public void setOnMakeMoveHandler(OnMakeMoveHandler h)
    {
        this.onMakeMoveHandler = h;
    }

    public Game.GameState getRole()
    {
        return role;
    }

    float getCornerX(RectF rect, int x)
    {
        return (float)x * rect.width() / 10 + rect.left;
    }

    float getCornerY(RectF rect, int y)
    {
        return (float)y / 10 * rect.height() + rect.top;
    }

    void drawShip(Canvas canvas, RectF rect, int x1, int y1, int x2, int y2, Paint p)
    {
        canvas.drawRoundRect(getCornerX(rect, x1), getCornerY(rect, y1), getCornerX(rect, x2 + 1), getCornerY(rect, y2 + 1), 50, 50, p);
    }

    void drawHit(Canvas canvas, RectF rect, int x, int y)
    {
        canvas.drawLine(getCornerX(rect, x), getCornerY(rect, y),
                getCornerX(rect, x + 1), getCornerY(rect, y + 1), hitPaint);

        canvas.drawLine(getCornerX(rect, x + 1), getCornerY(rect, y),
                getCornerX(rect, x), getCornerY(rect, y + 1), hitPaint);
    }

    void drawMiss(Canvas canvas, RectF rect, int x, int y)
    {
        canvas.drawLine(getCornerX(rect, x), getCornerY(rect, y),
                getCornerX(rect, x + 1), getCornerY(rect, y + 1), missPaint);

        canvas.drawLine(getCornerX(rect, x + 1), getCornerY(rect, y),
                getCornerX(rect, x), getCornerY(rect, y + 1), missPaint);
    }

    public void onUpdate()
    {
        invalidate();
    }

    public interface OnMakeMoveHandler
    {
        void onMakeMove(Game game);
    }
}
