package com;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

//* класс отвечающий за представление лабиринта, частично создано с помощью редактора форма NetBeans
public class FieldGUI extends JFrame {

    private JMenuItem testsMenuItem;

    //*Диалоговое окно, создания нового пустого лабиринта, предусмотрены методы на защиту ввода(недоступных данных)
    private class NewMazeDialog extends JDialog{
        private javax.swing.JButton okButton;
        private JLabel heightLabel;
        private JTextField heightTextField;
        private JLabel infoLabel1;
        private JLabel infoLabel2;
        private JLabel jLabel1;
        private JTextField weightTextField;
        private JLabel widthLabel;
        NewMazeDialog(Frame parent){
            super(parent,true);
            initGui();
            int sizeWidth = this.getWidth();
            int sizeHeight = this.getHeight();
            int locationX = (parent.getWidth() - sizeWidth) / 2;
            int locationY = (parent.getHeight() - sizeHeight) / 2;
            setLocation(parent.getX()+locationX, parent.getY() +locationY);
            setVisible(true);
        }

        //автоматическая инициализация формы
        private void initGui(){
            okButton = new javax.swing.JButton();
            jLabel1 = new javax.swing.JLabel();
            widthLabel = new javax.swing.JLabel();
            weightTextField = new JTextField();
            heightLabel = new JLabel();
            heightTextField = new JTextField();
            infoLabel1 = new javax.swing.JLabel();
            infoLabel2 = new javax.swing.JLabel();

            heightTextField.setTransferHandler(null);
            heightTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    checkTheTypingText(e);
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    int max = 45;
                    chekNumber(heightTextField,max);
                }
            });
            weightTextField.setTransferHandler(null);
            weightTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    checkTheTypingText(e);
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    int max = 85;
                    chekNumber(weightTextField,max);
                }
            });

            okButton.addActionListener(e -> {
                okButtonAction();
            });

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            jLabel1.setText("Введите ширину и высоту нового лабиринта");
            widthLabel.setText("Ширина:");
            heightLabel.setText("Высота");
            infoLabel1.setText("(макс. 85)");
            infoLabel2.setText("(макс. 45)");
            okButton.setText("ok");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap(19, Short.MAX_VALUE)
                                    .addComponent(jLabel1)
                                    .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(widthLabel)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(heightLabel)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(heightTextField)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(infoLabel1)
                                            .addComponent(infoLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(okButton)
                                    .addContainerGap())
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel1)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(widthLabel)
                                                            .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(infoLabel1))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                            .addComponent(heightLabel)
                                                            .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(infoLabel2)))
                                            .addGroup(layout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            pack();
        }
        //действие при нажатии на кнопку ок, с проверкой ошибок
        private void okButtonAction() {
            String errorMessage="";
            String height = heightTextField.getText();
            String weight = weightTextField.getText();
            if (height.length() > 0 && weight.length() > 0){
                int heightNum = Integer.parseInt(height);
                int weightNum = Integer.parseInt(weight);
                if (heightNum > 0 && weightNum > 0){
                    FieldGUI.this.clearField();
                    FieldGUI.this.createEmptyMazeArray(heightNum,weightNum);
                    FieldGUI.this.paintMaze();
                    dispose();
                    return;
                }
                else{
                    errorMessage = "Ошибка! Введено нулевое значение.";
                }
            }
            else {
                errorMessage = "Ошибка! Введите числа в оба поля.";
            }
            JOptionPane.showMessageDialog(this,errorMessage,"Ошибка",JOptionPane.ERROR_MESSAGE);
        }

        //далее идут два метода, которые защищают поля от неправильного ввода
        private void checkTheTypingText(KeyEvent e){
            if (!Character.isDigit(e.getKeyChar())) {
                e.consume();
                return;
            }
            return;
        }
        private void chekNumber(JTextField jTextField,int max){
            String s = jTextField.getText();
            int num;
            if (s.length()>0){
                num= Integer.parseInt(s);
            }
            else return;

            if(num>max){
                jTextField.setText(String.valueOf(max));
                return;
            }
            if(s.length()>2){
                jTextField.setText(String.valueOf(num));
                return;
            }
        }
    }

    private static class ColorIcon implements Icon {

        private int size;
        private Color color;

        public ColorIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillOval(x, y, size, size);
        }

        public Color getColor() {
            return color;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
    // класс, который модифицирует радиобатон, добавляя ей свойства цвета и состояния
    private static class CustomRadioButton extends JRadioButton{
        private Status status;
        public enum Status{
            BLOCK,EMPTY,START,FINISH,WAY;
         }
        public CustomRadioButton() {
            super();
            this.status = Status.EMPTY;
        }
        public CustomRadioButton(Status status) {
            super();
            this.status = status;
            changeColor(status);
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
            changeColor(status);
        }
        private void changeColor(Status status){
            switch (status) {
                case BLOCK -> {
                    this.setIcon(null );
                    this.setSelected(true);
                }
                case EMPTY -> {
                    this.setIcon(null );
                    this.setSelected(false);
                }
                case START -> {
                    this.setIcon(new ColorIcon(FieldGUI.RADIUSBUTTON,Color.GREEN));
                }
                case FINISH -> {
                    this.setIcon(new ColorIcon(FieldGUI.RADIUSBUTTON,Color.RED));
                }
                case WAY -> {
                    this.setIcon(new ColorIcon(FieldGUI.RADIUSBUTTON,Color.BLUE));
                }

            }
        }
    }

    //далее идут элементы интерфейса
    private JMenuItem newMazeMenuItem;
    private JMenuItem mazeFromFileMenuItem;
    private JMenuItem clearMenuItem;
    private JMenuItem guideMenuItem;
    private JMenuItem findMenuItem;
    private JMenuItem findShortestMenuItem;

    private JMenu mainMenu;
    private JMenu guideMenu;
    private JMenu runMenu;

    private JMenuBar menuBar;
    private JPanel mainPanel;
    private CustomRadioButton[][] mazeButtons;

    public final static int RADIUSBUTTON = 12;
    private boolean haveAStart = true;
    private boolean haveAFinish = true;

    public Maze maze;


    //конструктор
    public FieldGUI() {
        maze  = new Maze(new char[][]{{'S', 'X', 'X', 'X','X'}, {' ', ' ', ' ', ' ',' '}, {' ', ' ', 'X', 'X',' '}, {'X', ' ', ' ', 'X','F'}});
        initComponents();
        paintMaze();
    }

    //создания массива из Х-ов, и отправка массива в конструктор лабиринта(создается лабиринт из один стенок)
    public void createEmptyMazeArray(int height, int width){
        char[][] tempMaze = new char[height][width];
        for (int i = 0; i < tempMaze.length; i++) {
            for (int j = 0; j < tempMaze[0].length; j++) {
                tempMaze[i][j] = 'X';
            }
            maze = new Maze(tempMaze);
        }
    }

    // очистка поля(удаляет все элементы на основном поле)
    private void clearField(){
        haveAFinish = false;
        haveAStart = false;
        if (mazeButtons != null){
            for (int i = 0; i < mazeButtons.length; i++) {
                for (int j = 0; j < mazeButtons[0].length; j++) {
                    mainPanel.remove(mazeButtons[i][j]);
                }
            }
        }
    }

    //событие, которое меняет цвет и состояние точки, при нажатии на нее
    private void changeState(ActionEvent e) {
        CustomRadioButton jrb = (CustomRadioButton)e.getSource();
        if (jrb.getStatus()== CustomRadioButton.Status.BLOCK){
            if(!haveAStart){
                jrb.setStatus(CustomRadioButton.Status.START);
                haveAStart=true;
                return;
            }
            if(!haveAFinish){
                jrb.setStatus(CustomRadioButton.Status.FINISH);
                haveAFinish = true;
                return;
            }
            jrb.setStatus(CustomRadioButton.Status.EMPTY);
            return;
        }
        if (jrb.getStatus()== CustomRadioButton.Status.START) {
            haveAStart = false;
            if (!haveAFinish) {
                jrb.setStatus(CustomRadioButton.Status.FINISH);
                haveAFinish = true;
                return;
            }
            jrb.setStatus(CustomRadioButton.Status.EMPTY);
            return;
        }
        if (jrb.getStatus()== CustomRadioButton.Status.FINISH) {
            haveAFinish = false;
            jrb.setStatus(CustomRadioButton.Status.EMPTY);
            return;
        }
        if(jrb.getStatus() == CustomRadioButton.Status.EMPTY || jrb.getStatus() == CustomRadioButton.Status.WAY ){
            jrb.setStatus(CustomRadioButton.Status.BLOCK);
        }
    }


    //рисует точки исходя из данных объекта лабиринта(каждое состояние, соотвествует своей точке
    private void paintMaze(){
        mazeButtons = new CustomRadioButton[maze.getTheMaze().length][maze.getTheMaze()[0].length];
        MigLayout migLayout = new MigLayout("wrap "+maze.getCols()+", center center,center center");
        mainPanel.setLayout(migLayout);
        for (int i = 0; i < mazeButtons.length; i++) {
            for (int j = 0; j < mazeButtons[0].length; j++) {
                switch (maze.getTheMaze()[i][j]){
                    case 'X' -> mazeButtons[i][j] = new CustomRadioButton(CustomRadioButton.Status.BLOCK);
                    case ' ' -> mazeButtons[i][j] = new CustomRadioButton(CustomRadioButton.Status.EMPTY);
                    case 'S' -> mazeButtons[i][j] = new CustomRadioButton(CustomRadioButton.Status.START);
                    case 'F' -> mazeButtons[i][j] = new CustomRadioButton(CustomRadioButton.Status.FINISH);
                    case  '.' -> mazeButtons[i][j] = new CustomRadioButton(CustomRadioButton.Status.WAY);
                }
                mazeButtons[i][j].addActionListener(this::changeState);
                mazeButtons[i][j].setMargin(new Insets(0,0,0,0));
                mainPanel.add(mazeButtons[i][j]);
                mazeButtons[i][j].setVisible(true);
            }
        }
        mainPanel.setVisible(false);
        java.awt.EventQueue.invokeLater(this::repaint);
        mainPanel.setVisible(true);
    }

    //открытие окна создания пустого лабиринта
    private void createNewMazeAction(){
        new NewMazeDialog(this);
    }
    //полу-сгенерированный метод, рисующей все элементы в интерфейсе
    private void initComponents() {
        setMinimumSize(new Dimension(600,400));
        mainPanel = new JPanel();

        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        guideMenu = new JMenu();
        runMenu = new JMenu();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainMenu.setText("Главное меню");
        menuBar.add(mainMenu);

        newMazeMenuItem = new JMenuItem("Создать новый лабиринт");
        newMazeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewMazeAction();
            }
        });
        mazeFromFileMenuItem = new JMenuItem("Считать лабиринт из файла");
        mainMenu.add(newMazeMenuItem);
        mainMenu.add(mazeFromFileMenuItem);
        mazeFromFileMenuItem.addActionListener(e -> mazeChooseFromFileAction());

        runMenu.setText("Запуск");
        menuBar.add(runMenu);

        findMenuItem = new JMenuItem("Найти случайный путь");
        findMenuItem.addActionListener((e -> newSolve(true)));
        findShortestMenuItem = new JMenuItem("Найти кратчайший путь");
        findShortestMenuItem.addActionListener(e -> newSolve(false));
        testsMenuItem= new JMenuItem("Провести тесты производительности");
        testsMenuItem.addActionListener(e -> testsAction());
        findMenuItem.addActionListener((e -> newSolve(true)));
        clearMenuItem = new JMenuItem("Очистить лабиринт");
        runMenu.add(findMenuItem);
        runMenu.add(findShortestMenuItem);
        runMenu.add(clearMenuItem);
        runMenu.add(testsMenuItem);

        clearMenuItem.addActionListener((e)->clearMaze());
        guideMenu.setText("Руководство");
        menuBar.add(guideMenu);

        guideMenuItem = new JMenuItem("Руководство");
        guideMenuItem.addActionListener(e -> openGuideAction());
        guideMenu.add(guideMenuItem);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        pack();
    }

    // событие открывающее руководство пользователя
    private void openGuideAction() {
        Desktop desktop = Desktop.getDesktop();
        try {
            File file = new File("resource\\Руководство пользователя.docx");
            desktop.open(file);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //событие открывающее тесты производительности
    private void testsAction() {
        char[][] tempSize = fillTemp();
        if(!maze.setTheMaze(maze.getMazeForSolve(tempSize))){
            JOptionPane.showMessageDialog(this,maze.getError(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!maze.depthFirstSearch(maze.getRowStart(),maze.getColStart())){
            maze.setTheMaze(tempSize);
            String warning = "Путь не найден!";
            JOptionPane.showMessageDialog(this,warning,"Результаты",JOptionPane.WARNING_MESSAGE);
        }
        else {
            long timeWave = System.nanoTime();
            maze.waveSearch(maze.getRowStart(),maze.getColStart());
            timeWave = System.nanoTime()-timeWave;
            maze.setTheMaze(maze.getMazeForSolve(tempSize));

            long timeDepth = System.nanoTime();
            maze.depthFirstSearch(maze.getRowStart(),maze.getColStart());
            timeDepth = System.nanoTime()-timeDepth;
            maze.setTheMaze(maze.getMazeForSolve(tempSize));

            long timeBreadth = System.nanoTime();
            maze.BFC(maze.getRowStart(),maze.getColStart());
            timeBreadth = System.nanoTime()-timeBreadth;
            maze.setTheMaze(maze.getMazeForSolve(tempSize));

            String info = "Путь найден! Результаты тестов:\n\nПоиск в глубину: "+ timeDepth +" нс.\nПоиск в ширину: "+timeBreadth+" нс.\nПоиск волнами: "+timeWave+" нс.";
            JOptionPane.showMessageDialog(this,info,"Результаты",JOptionPane.INFORMATION_MESSAGE);
        }

    }

    //событие запуска поиска пути из класса Лабирнт. В зависимости от флага isRandomSolve меняется алгоритм поиска
    private void newSolve(boolean isRandomSolve){
        char[][] tempSize = fillTemp();
        if(!maze.setTheMaze(maze.getMazeForSolve(tempSize))){
            JOptionPane.showMessageDialog(this,maze.getError(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!isRandomSolve){
            if(!maze.BFC(maze.getRowStart(),maze.getColStart())){
                maze.setTheMaze(tempSize);
                String warning = "Путь не найден!";
                JOptionPane.showMessageDialog(this,warning,"Результаты",JOptionPane.WARNING_MESSAGE);
            }
            else {
                maze.setTheMaze(maze.getMazeFromSolve(maze.getTheMaze()));
                maze.writeToFile();
            }
        }
        else{
            if(!maze.depthFirstSearch(maze.getRowStart(),maze.getColStart())){
                maze.setTheMaze(tempSize);
                String warning = "Путь не найден!";
                JOptionPane.showMessageDialog(this,warning,"Результаты",JOptionPane.WARNING_MESSAGE);
            }
            else {
                maze.setTheMaze(maze.getMazeFromSolve(maze.getTheMaze()));
                maze.writeToFile();
            }
        }

        clearField();
        paintMaze();
        haveAStart = maze.isHaveStart();
        haveAFinish = maze.isHaveFinish();
    }


    //заполнить массив исходя из состояния точек лабиринта
    private char[][] fillTemp(){
        haveAStart = false;
        haveAFinish = false;
        char[][] tempMaze = new char[mazeButtons.length][mazeButtons[0].length];
        for (int i = 0; i <tempMaze.length ; i++) {
            for (int j = 0; j <tempMaze[0].length ; j++) {
                switch (mazeButtons[i][j].getStatus()){
                    case EMPTY,WAY -> tempMaze[i][j] = ' ';
                    case BLOCK -> tempMaze[i][j] = 'X';
                    case FINISH -> {
                        tempMaze[i][j] = 'F';
                        haveAFinish = true;
                    }
                    case START -> {
                        tempMaze[i][j] = 'S';
                        haveAStart = true;
                    }
                }
            }
        }
        return tempMaze;
    }
    // очистить массив лабиринта в объекте Лабиринта
    private void clearMaze() {
        if (maze == null) return;
        for (int i = 0; i < maze.getRows(); i++) {
            for (int j = 0; j < maze.getCols(); j++) {
                maze.getTheMaze()[i][j] = ' ';
            }
        }
        clearField();
        haveAFinish = false;
        haveAStart = false;
        paintMaze();
    }


    //события выбора лабиринта из файла
    private void mazeChooseFromFileAction() {
        JFileChooser jf = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        jf.setFileFilter(filter);
        String path;
        if (jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            path = jf.getSelectedFile().getAbsolutePath();
        }
        else {
            return;
        }
        char[][] tempMaze = maze.getTheMaze();
        maze = new Maze(path);
        haveAFinish = maze.isHaveFinish();
        haveAStart = maze.isHaveStart();
        if (maze.getError().contains("\b")){
            JOptionPane.showMessageDialog(this,maze.getError(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            maze.setTheMaze(tempMaze);
            return;
        }
        else if (maze.getError().length()>0) {
            JOptionPane.showMessageDialog(this,maze.getError(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            clearField();
            paintMaze();
        }
        else {
            clearField();
            paintMaze();
        }
    }
}
