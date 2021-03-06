package com.dfsek.terra.ui;

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.ansi.UnixLikeTerminal;

import java.io.IOException;

/**
 * Unused.
 * <p>
 * Was originally going to do a terminal UI, but decided not to.
 * May still do that in the future.
 */
public class TerminalUIEngine {
    private final Screen screen;

    public TerminalUIEngine() throws IOException {
        this(new DefaultTerminalFactory());
//        System.setProperty("com.googlecode.lanterna.terminal.UnixTerminal.catchSpecialCharacters", "false");
    }

    public TerminalUIEngine(DefaultTerminalFactory factory) throws IOException {
        factory.setUnixTerminalCtrlCBehaviour(UnixLikeTerminal.CtrlCBehaviour.TRAP);

//        TerminalSize size = new TerminalSize(80, 24);
//        factory.setInitialTerminalSize(size);
        screen = factory.createScreen();
        screen.startScreen();

        Runtime.getRuntime().addShutdownHook(new TerminalShutdownHook(screen));
    }

    @SuppressWarnings("RedundantThrows")
    public void startMainUi() throws IOException, InterruptedException {
        drawBaseUi();

        while(true) {
//            screen.refresh();
            // Handle exit keystroke first
            KeyStroke keystroke = screen.pollInput();
            if(keystroke != null &&
                    (keystroke.getKeyType() == KeyType.Escape || keystroke.getKeyType() == KeyType.EOF ||
                            (keystroke.isCtrlDown() && keystroke.getKeyType() == KeyType.Character && keystroke.getCharacter() == 'c'))) {
                screen.close();
                System.exit(0);
                break;
            }

            if(screen.doResizeIfNecessary() != null)
                drawBaseUi();
        }

    }

    private void drawBaseUi() throws IOException {
        int width = screen.getTerminalSize().getColumns() - 1;
        int height = screen.getTerminalSize().getRows() - 1;

        if(width + 1 < 80 || height + 1 < 24) {
            screen.close();
            System.err.println("Your terminal must be at least 80x24. Please run with --no-gui if this is not possible.");
            System.exit(1);
        }

        screen.setCursorPosition(null);

        screen.clear();

        TextGraphics graphics = screen.newTextGraphics();

        // Progress box
        drawBoxWithText(0, 0, width, 2, "progress", graphics);

        // Threads box
        int threadWidth = width * 5 / 16;
        int threadHeight = (height - 3) * 5 / 11;
        drawBoxWithText(0, 3, threadWidth, threadHeight, "threads", graphics);

        // Profiler box
        int profilerWidth = width * 17 / 40;
        drawBoxWithText(threadWidth + 1, 3, threadWidth + profilerWidth, threadHeight, "profiler", graphics);

        // Graph box
        drawBoxWithText(threadWidth + profilerWidth + 1, 3, width, threadHeight, "graph", graphics);

        // Advanced statistics box
        int statisticsHeight = (height - 3) * 5 / 22;
        drawBoxWithText(0, threadHeight + 1, width / 2, threadHeight + statisticsHeight, "advanced statistics", graphics);

        // Performance statistics box
        drawBoxWithText(width / 2 + 1, threadHeight + 1, width, threadHeight + statisticsHeight, "performance statistics", graphics);

        // Logs box
        drawBoxWithText(0, threadHeight + statisticsHeight + 1, width, height, "logs", graphics);

        screen.refresh();
    }

    private void drawBoxWithText(int fromX, int fromY, int toX, int toY, String text, TextGraphics graphics) {
        graphics.setCharacter(fromX, fromY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_TOP_LEFT_CORNER)[0]); // top left corner
        graphics.setCharacter(toX, fromY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_TOP_RIGHT_CORNER)[0]); // top right corner
        graphics.setCharacter(fromX, toY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER)[0]); // bottom left corner
        graphics.setCharacter(toX, toY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER)[0]); // bottom right corner


        graphics.setCharacter(fromX + 1, fromY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_HORIZONTAL)[0]); // mini line
        graphics.setCharacter(fromX + 2, fromY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_T_LEFT)[0]); // text left delimiter
        graphics.putCSIStyledString(fromX + 3, fromY, text); // text
        graphics.setCharacter(fromX + 3 + text.length(), fromY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_T_RIGHT)[0]); // text right delimiter

        graphics.drawLine(fromX + text.length() + 4, fromY, toX - 1, fromY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_HORIZONTAL)[0]); // top line

        graphics.drawLine(fromX + 1, toY, toX - 1, toY, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_HORIZONTAL)[0]); // bottom line

        graphics.drawLine(fromX, fromY + 1, fromX, toY - 1, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_VERTICAL)[0]); // left line

        graphics.drawLine(toX, fromY + 1, toX, toY - 1, TextCharacter.fromCharacter(Symbols.SINGLE_LINE_VERTICAL)[0]); // right line
    }

    public static class TerminalShutdownHook extends Thread {
        private final Screen screen;

        public TerminalShutdownHook(Screen screen) {
            this.screen = screen;
        }

        @Override
        public void run() {
            try {
                screen.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
