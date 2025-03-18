package com.github.guilleleon.javarush.buscaminas;

import com.javarush.engine.cell.*;

import java.util.*;

public class MinesweeperGame extends Game {
    
    private List<Boolean> cellsValues = new ArrayList(); 
    private List<Boolean> cellsMarkValues = new ArrayList();
    private int safeHidden = 0;
    private int safeFound = 0;
    
    @Override
    public void initialize() {
        setScreenSize(6, 6);
        
        // showGrid(false);
        drawTable();
    }
    private void drawTable() {
        for (int f = 0; f < getScreenHeight(); f++) {
            for (int c = 0; c < getScreenWidth()-1; c++) {
                setHide(c, f);
                
                setCellValueEx(getScreenWidth() -1, 0, Color.WHITE, "Restart", Color.BLUE);
                
                int rand = getRandomNumber(0, 9);
                int cellIndex = (f * (getScreenWidth()-1)) + c;
                boolean mined = rand>=6;
                if (mined) {
                    safeHidden++;
                }
                cellsValues.add(cellIndex, mined);
                
                cellsMarkValues.add(cellIndex, false);
            }
            // setHide(getScreenWidth(), f);
        }
    }
    
    @Override
    public void onMouseLeftClick(int x, int y) {
        if (x == getScreenWidth()-1) {
            drawTable();
            return;
        }
        int cellIndex = (y * (getScreenWidth()-1)) + x;
        boolean cellIsMined = cellsValues.get(cellIndex);
        if (cellIsMined) {
            setMine(x, y);
            showMessageDialog(
                Color.RED, "Perdiste, pisaste una mina!!", Color.WHITE, 80
            );
        } else {
            setSafe(x, y, getMinesNearCount(x, y));
            safeFound++;
            if (safeFound >= safeHidden) {
                showMessageDialog(
                    Color.RED, "Ganaste master!!", Color.WHITE, 80
                );
            }
        }
    }
    @Override
    public void onMouseRightClick(int x, int y) {
        int cellIndex = (y * (getScreenWidth()-1)) + x;
        if (cellsMarkValues.get(cellIndex) == false) {
            markCellAsPosibleMine(x, y);
            cellsMarkValues.set(cellIndex, true);
        } else {
            setHide(x, y);
            cellsMarkValues.set(cellIndex, false);
        }
    }
    
    private void setMine(int x, int y) {
        setCellValueEx(x, y, Color.RED, "M", Color.YELLOW);
    }
    private void setSafe(int x, int y, int mines) {
        setCellValueEx(x, y, Color.WHITE, String.valueOf(mines), Color.ORANGE);
    }
    private void setHide(int x, int y) {
        setCellValueEx(x, y, Color.GRAY, "?", Color.BLACK);
    }
    private void markCellAsPosibleMine(int x, int y) {
        setCellValueEx(x, y, Color.ORANGE, "!", Color.BLACK);
    }
    
    private int getMinesNearCount(int x, int y) {
        int mines = 0;
        int[] desplazamientoX = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] desplazamientoY = { -1, 0, 1, -1, 1, -1, 0, 1 };
        for (int i = 0; i < 8; i++) {
            int newX = x + desplazamientoX[i];
            int newY = y + desplazamientoY[i];
            
            if (newX < 0 || newX >= getScreenWidth()-1) continue;
            if (newY < 0 || newY >= getScreenHeight()) continue;
            
            int cellIndex = (newY * (getScreenWidth()-1)) + newX;
            if (cellsValues.get(cellIndex)) {
                mines++;
            }
        }
        
        return mines;
    }
}
