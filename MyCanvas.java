import java.awt.Canvas;
import java.awt.Graphics;

public class MyCanvas extends Canvas {
	public static final long serialVersionUID = 2L;

	private VisualizerProvider listener;

	public MyCanvas(VisualizerProvider listener)
	{
		super();
		this.listener = listener;
	}

    public void paint(Graphics g)
    {
        super.paint(g);
		clear(g);

		listener.onDrawArray();
    }


	public void clear(Graphics g)
	{
		g.setColor(ColorManager.CANVAS_BACKGROUND);
        g.fillRect(0, 0, getWidth(), getHeight());
	}


	public interface VisualizerProvider
	{
		void onDrawArray();
	}
}
