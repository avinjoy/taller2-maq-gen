package editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;

class JaggedHighlighterPainter extends LayeredHighlighter.LayerPainter {

    private final String comment;
    
    public JaggedHighlighterPainter(Color color,String c) {
        
        comment = c;
    }

    @Override
    public void paint(Graphics g, int p0, int p1, Shape allocation, JTextComponent c) {

        paintJaggedLine(g, allocation);
    }

    public void paintJaggedLine(Graphics g, Shape a) {
        int y = (int) (a.getBounds().getY() + a.getBounds().getHeight());
        int x1 = (int) a.getBounds().getX();
        int x2 = (int) (a.getBounds().getX() + a.getBounds().getWidth());
        Color old = g.getColor();
        g.setColor(Color.red);
        for (int i = x1; i < (x2 - 6); i += 3) {
            g.drawArc(i, y - 4, 3, 3, 0, 180);
            g.drawArc(i + 3, y - 3, 3, 3, 180, 181);
        }
        g.setColor(old);
    }

    @Override
    public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view) {
        try {
            paintJaggedLine(g, view.modelToView(p0, Position.Bias.Backward, p1, Position.Bias.Forward, viewBounds));
        } catch (BadLocationException ex) {
            Logger.getLogger(JaggedHighlighterPainter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return viewBounds;
    }
    
    @Override
    public String toString(){
        
        return comment;
    }
}