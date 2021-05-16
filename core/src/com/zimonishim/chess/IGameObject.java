package com.zimonishim.chess;

public interface IGameObject {

    /**
     * Contains code related to values on the screen. Should be called every frame.
     */
    public void update();

    /**
     * Contains code directly related to drawing graphics on the screen. Should be called every frame.
     * @param drawCallback  callback containing the Batch and ShapeDrawer to draw with
     */
    public void draw(IDrawCallback drawCallback);
}