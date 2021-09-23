package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 Класс лабиринта, здесь реализованый все методы поиска, построение пути,
 создания лабиринта, запись лабиринта файл, чтение лабиринта из файла и др.
**/
public class Maze {

    private boolean isHaveStart = false; //поле отвечающее есть ли старт в лабиринте
    private boolean isHaveFinish = false;//поле отвечающее есть ли финиш в лабиринте
    private String error = "";
    private char[][] theMaze; //сам лабиринт (x-блок; _ - пусто; s - старт; f -финиш; . - путь)
    private int colStart, rowStart; // координаты старта
    private int rows, cols; // колво строчек и столбцов
    private String outputFilename; // имя выходного файла
    public Maze(char[][] theMaze){ // коструктор создания лабирнта по массиву
        setTheMaze(theMaze);
        this.outputFilename = "someMaze.txt";
        //solve(this.rowStart,this.colStart);
    }
    public Maze(String filename) {// коструктор создания лабирнта по файлу

        try {
            this.outputFilename = filename;
            Scanner scan = new Scanner(new File(filename));
            StringBuilder sb = new StringBuilder();
            while (scan.hasNext()) {
                sb.append(scan.nextLine());
                this.rows++;
            }
            this.cols = sb.length() / this.rows;
            this.theMaze = new char[this.rows][this.cols];
            int m = 0;
            System.out.println();
            //Чтение из файла, с проверкой неккоректности формата(вторые найденные вход и выход будет заменяться на пробел)
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.cols; j++) {
                    char sym = sb.charAt(m);
                    if (sym!='X' && sym!=' ' && sym!='S' && sym!='F'){
                        error = "\bОшибка! Неверный формат текстового файла!";
                        scan.close();
                        return;
                    }
                    if(sym=='S'){
                        if (isHaveStart){
                            error = "Ошибка формата! Должен быть один вход!";
                            sym  = ' ';
                        }
                        isHaveStart = true;
                    }
                    if(sym=='F'){
                        if (isHaveFinish){
                            error = "Ошибка формата! Должен быть один выход!";
                            sym  = ' ';
                        }
                        isHaveFinish = true;
                    }
                    theMaze[i][j] = sym;
                    m++;
                }
            }
            scan.close();
            findStart();
        } catch (FileNotFoundException e) {
            error = "\bОшибка! Неккоректный файл!";
        }
    }

    public boolean setTheMaze(char[][] theMaze) { // метод замены массива лабиринта
        isHaveFinish = false;
        isHaveStart = false;
        boolean flag = true;
        for (int i = 0; i < theMaze.length; i++) {
            for (int j = 0; j < theMaze[0].length; j++) {
                if (theMaze[i][j]=='S'){
                    if(isHaveStart){
                        theMaze[i][j]=' ';
                        error = "Ошибка формата! Должен быть один вход!";
                        flag = false;
                    }
                    isHaveStart=true;
                }
                if (theMaze[i][j]=='F'){
                    if(isHaveFinish){
                        theMaze[i][j]=' ';
                        error = "Ошибка формата! Должен быть один выход!";
                        flag = false;
                    }
                    isHaveFinish=true;
                }
            }
        }
        if(!isHaveFinish || !isHaveStart){
            error = "Ошибка формата! Отсутсвует вход или выход!";
            flag = false;
        }
        this.theMaze = theMaze;
        cols = theMaze[0].length;
        rows = theMaze.length;
        findStart();
        return flag;
    }

    /**
     * instantiate the index value value of 'S' to this.rowStart, this.colStart
     */
    private void findStart() { // метод поиска старта
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (theMaze[i][j] == 'S') {
                    this.rowStart = i;
                    this.colStart = j;
                }
            }
        }
    }

    public char[][] getMazeForSolve(char[][] theMaze){ // получить лабирнт для решения
        // (т.е взять текущий лабирнт и обставить его по кругу стенкой)
        // требуется для поиска путей
        char[][] tempMaze = new char[theMaze.length+2][theMaze[0].length+2];
        for (int i = 0; i < tempMaze.length; i++) {
            for (int j = 0; j < tempMaze[0].length; j++) {
                if(i==0 || i == tempMaze.length-1 || j == 0 || j==tempMaze[0].length-1  ){
                    tempMaze[i][j] = 'X';
                    continue;
                }
                    tempMaze[i][j]=theMaze[i-1][j-1];
            }
        }
        return tempMaze;
    }

    public char[][] getMazeFromSolve(char[][] theMaze){ // получить алгоритм после решения, т.е убрать окружающие стены
        char[][] tempMaze = new char[theMaze.length-2][theMaze[0].length-2];
        for (int i = 0; i < tempMaze.length; i++) {
            for (int j = 0; j < tempMaze[0].length; j++) {
                tempMaze[i][j]=theMaze[i+1][j+1];
            }
        }
        return tempMaze;
    }
    //

    //*далее идет метод поиска ширину и все вспомогательные методы
    private boolean isVisited(int[] couple){
        return visited[couple[0]][couple[1]] || theMaze[couple[0]][couple[1]] == 'X';
    }
    private void setVisited(int[] couple){
        visited[couple[0]][couple[1]]  = true;
    }
    private boolean isFinish(int[] couple){
       return theMaze[couple[0]][couple[1]] == 'F';
    }

    private boolean[][] visited;
    private LinkedList<int[]> queue;
    
    private void setShortestWay(LinkedList<int[][]> way,int[] finish){
        theMaze[finish[0]][finish[1]] = '.';
        while (theMaze[finish[0]][finish[1]] != 'S'){
            theMaze[finish[0]][finish[1]] = '.';
            for (int[][] spots : way) {
                if(Arrays.equals(finish, spots[0])){
                    finish = spots[1];
                    way.remove(spots);
                    break;
                }
            }
        }
    }
    public  boolean BFC(int row,int col){
        LinkedList<int[][]> nodesAndAncestors = new LinkedList<>();
        visited = new boolean[rows][cols];
        queue = new LinkedList<>();
        queue.offer(new int[]{row,col});

        while (!queue.isEmpty()){
            int[] v  = queue.poll();
            int[]right = new int[]{v[0],v[1]+1};
            int[] left = new int[]{v[0],v[1]-1};
            int[] up = new int[]{v[0]+1,v[1]};
            int[] down = new int[]{v[0]-1,v[1]};

            if(!isVisited(right)){
                queue.offer(right);
                nodesAndAncestors.add(new int[][]{right,v});
                if(isFinish(right)){
                    setShortestWay(nodesAndAncestors,v);
                    return true;
                }

                setVisited(right);
            }
            if(!isVisited(left)){
                queue.offer(left);
                nodesAndAncestors.add(new int[][]{left,v});
                if(isFinish(left)){
                    setShortestWay(nodesAndAncestors,v);
                    return true;
                }
                setVisited(left);
            }
            if(!isVisited(up)){
                queue.offer(up);
                nodesAndAncestors.add(new int[][]{up,v});
                if(isFinish(up)) {
                    setShortestWay(nodesAndAncestors,v);
                    return true;
                }
                setVisited(up);
            }
            if(!isVisited(down)){
                queue.offer(down);
                nodesAndAncestors.add(new int[][]{down,v});
                up[1] = v[1];
                if(isFinish(down)) {
                    setShortestWay(nodesAndAncestors,v);
                    return true;
                }
                setVisited(down);
            }
        }
        return false;
    }
    //*далее идет метод поиска волнами и метод получения пути
    public boolean waveSearch (int row,int col){
        int[][] markedNode  = new int[rows][cols];
        int markNumber = 1;
        markedNode[row][col] = markNumber;
        while (true){//нашли конец
            for (int i = 0; i < markedNode.length; i++) {
                for (int j = 0; j < markedNode[0].length; j++) {
                    if (markNumber == markedNode[i][j]){
                        if (theMaze[i][j]=='F') {
                            pathRecoveryWave(new int[]{row,col},new int[]{i,j},markedNode);
                            return true;
                        }
                        int[]right = new int[]{i,j+1};
                        int[] left = new int[]{i,j-1};
                        int[] up = new int[]{i+1,j};
                        int[] down = new int[]{i-1,j};
                        if(markedNode[right[0]][right[1]]==0 && theMaze[right[0]][right[1]]!='X'){
                            markedNode[right[0]][right[1]] = markNumber+1;
                        }
                        if(markedNode[left[0]][left[1]]==0 && theMaze[left[0]][left[1]]!='X'){
                            markedNode[left[0]][left[1]] = markNumber+1;
                        }
                        if(markedNode[up[0]][up[1]]==0 && theMaze[up[0]][up[1]]!='X'){
                            markedNode[up[0]][up[1]] = markNumber+1;
                        }
                        if(markedNode[down[0]][down[1]]==0 && theMaze[down[0]][down[1]]!='X'){
                            markedNode[down[0]][down[1]] = markNumber+1;
                        }
                    }
                }
            }
            markNumber++;
        }
    }
    private void pathRecoveryWave(int[] from, int[] to, int[][] markedNode){
        int[] spot = to;
        while (true) {
            int[] right = new int[]{spot[0], spot[1] + 1};
            int[] left = new int[]{spot[0], spot[1] - 1};
            int[] up = new int[]{spot[0] + 1, spot[1]};
            int[] down = new int[]{spot[0] - 1, spot[1]};
            if (markedNode[spot[0]][spot[1]] - 1 == markedNode[right[0]][right[1]]) {
                spot = right;

            }
            if (markedNode[spot[0]][spot[1]] - 1 == markedNode[left[0]][left[1]]) {
                spot = left;

            }
            if (markedNode[spot[0]][spot[1]] - 1 == markedNode[up[0]][up[1]]) {
                spot = up;
            }
            if (markedNode[spot[0]][spot[1]] - 1 == markedNode[down[0]][down[1]]) {
                spot = down;

            }
            if(theMaze[spot[0]][spot[1]] != 'S'){
                theMaze[spot[0]][spot[1]] = '.';
            }
            else {
                break;
            }
            theMaze[spot[0]][spot[1]] = '.';
        }
        }
        //* метод записи в файл
    public void writeToFile(){
            try {
                File file = new File(this.outputFilename.substring(0,this.outputFilename.lastIndexOf("."))+ " solved.txt");
                PrintWriter writer = new PrintWriter(file);
                for (int i = 0; i < this.rows; i++) {
                    for (int j = 0; j < this.cols; j++) {
                        writer.print(this.theMaze[i][j]);
                    }
                    writer.println();
                }
                writer.close();
            } catch (FileNotFoundException e) {
               e.printStackTrace();
            }
    }
    // метод поиска в глубину и все прилегающие методы
    public boolean depthFirstSearch(int row, int col) {
        char right = this.theMaze[row][col + 1];
        char left = this.theMaze[row][col - 1];
        char up = this.theMaze[row - 1][col];
        char down = this.theMaze[row + 1][col];

        if (right == 'F' || left == 'F' || up == 'F' || down == 'F') {
            if(this.theMaze[row][col]!='S'){
                this.theMaze[row][col] = '.';
            }
            return true;
        }

        boolean solved = false;
        if (this.theMaze[row][col] != 'S') {
            this.theMaze[row][col] = '.'; // we don't want to mess up by changing value of 'S' when we start our journey.
        }
        if (right == ' ' && !solved) {
            solved = depthFirstSearch(row, col + 1);
        }
        if (down == ' ' && !solved) {
            solved = depthFirstSearch(row + 1, col);
        }
        if (left == ' ' && !solved) {
            solved = depthFirstSearch(row, col - 1);
        }
        if (up == ' ' && !solved) {
            solved = depthFirstSearch(row - 1, col);
        }
        if (!solved) {
            this.theMaze[row][col] = ' ';
        }
        return solved;

    }
    // гетер и сетеры
    public boolean isHaveStart() {
        return isHaveStart;
    }

    public boolean isHaveFinish() {
        return isHaveFinish;
    }

    public int getColStart() {
        return colStart;
    }

    public int getRowStart() {
        return rowStart;
    }

    public int getCols() {
        return cols;
    }

    public char[][] getTheMaze() {
        return theMaze;
    }

    public int getRows() {
        return rows;
    }

    public String getError() {
        return error;
    }
}