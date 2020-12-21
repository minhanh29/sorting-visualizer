import javax.swing.text.NumberFormatter;
import java.text.ParseException;
import java.text.NumberFormat;

public class MyFormatter extends NumberFormatter
{
	public static final long serialVersionUID = 1L;

	public MyFormatter(NumberFormat format)
	{
		super(format);
	}

	public Object stringToValue(String text) throws ParseException
	{
		if ("".equals(text))
			return 0;
		return super.stringToValue(text);
	}
}
