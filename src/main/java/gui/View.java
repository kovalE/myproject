package gui;

public class View {
	private static View instance = new View();
	
	private MainWindow mw = new MainWindow();
	
	public static View getInstance() {
		return instance;
	}
	
	private View(){
	}
	
	public MainWindow getMW() {
		return mw;
	}

	public void setMW(MainWindow mw) {
		this.mw = mw;
	}
}
