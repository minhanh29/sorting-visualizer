import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class MyCanvas extends Canvas {
	public static final long serialVersionUID = 2L;

	private Visualizer drawer;

	public MyCanvas()
	{
		super();
	}

    public void paint(Graphics g)
    {
        super.paint(g);
		clear(g);

		if (drawer != null)
			drawer.drawArray();
    }


	public void clear(Graphics g)
	{
		g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
	}


	public void setVisualizer(Visualizer drawer) { this.drawer = drawer; }
}
