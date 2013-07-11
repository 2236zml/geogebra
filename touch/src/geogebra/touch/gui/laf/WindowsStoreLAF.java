package geogebra.touch.gui.laf;

public class WindowsStoreLAF extends DefaultLAF
{
	@Override
	public void setTitle(String title)
	{

	}

	@Override
	public int getPanelsHeight()
	{
		return 60;
	}

	@Override
	public int getAppBarHeight()
	{
		return 0;
	}

	@Override
  public DefaultIcons getIcons()
  {
	  // FIXME return windows specific icons
	  return DefaultIcons.INSTANCE;
  }
}
