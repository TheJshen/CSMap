package com.example.kcco.csmap.Misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.jar.Attributes;

/**
 * Created by KaChan on 5/20/2015.
 */
public class FlyOutContainer extends LinearLayout{

    // References to groups contained in this view.
    private View menu;
    private View content;

    // Constants
    protected static int menuMargin =  150;

    // State will be mroe for animations
    public enum MenuState {
        CLOSED, OPEN
    }

    //Position information attributes
    protected int currentContentOffset = 0;
    protected MenuState menuCurrentState = MenuState.CLOSED; // Menu default is closed

    public FlyOutContainer(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        menuMargin = this.getHeight() - 150;
    }

    public FlyOutContainer(Context context, AttributeSet attrs){
        super(context, attrs);
        menuMargin = this.getHeight() - 150;
    }

    public FlyOutContainer(Context context){
        super(context);
        menuMargin = this.getHeight() - 150;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //assume 1st layout is menu in activity.xml
        //assume 2nd layout is content in activity.xml
        this.menu = this.getChildAt(0);
        this.content = this.getChildAt(1);

        //Default not to draw menu
        this.menu.setVisibility(View.GONE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        if(changed)
            this.calculateChildDimensions();

        this.menu.layout(left, top, right, bottom - menuMargin);
        this.content.layout(left, top + this.currentContentOffset, right
                                , bottom + this.currentContentOffset);
    }

    public void toogleMenu(){
        //change this object value in according to current menu state
        switch (this.menuCurrentState){
            case CLOSED:
                this.menu.setVisibility(View.VISIBLE);
                this.currentContentOffset = this.getMenuHeight();
                this.content.offsetTopAndBottom(currentContentOffset);
                this.menuCurrentState = MenuState.OPEN;
                break;
            case OPEN:
                this.content.offsetTopAndBottom(currentContentOffset);
                this.currentContentOffset = 0;
                this.menuCurrentState = MenuState.CLOSED;
                this.menu.setVisibility(View.GONE);
                break;
        }

        this.invalidate();
    }

    private int getMenuWidth(){
        return this.menu.getLayoutParams().width;
    }

    private int getMenuHeight(){
        return this.menu.getLayoutParams().height;
    }

    private void calculateChildDimensions(){
        this.content.getLayoutParams().height = this.getHeight();
        this.content.getLayoutParams().width = this.getWidth();

        this.menu.getLayoutParams().width = this.getWidth();
        this.menu.getLayoutParams().height = 150;
    }


}
