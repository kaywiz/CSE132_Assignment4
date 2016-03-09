package assignment4;

import java.io.*;

import javax.swing.JFrame;

import studio4.PrintStreamPanel;

public class ViewInputStream extends FilterInputStream {

	final private PrintStreamPanel psp;
	final private PrintStream ps;

	public enum State {
		waitingForMagicNumber, waitingForInputType, readingDebugString, readingErrorString, readingTimestamp, readingPoten, readingRawTemp, readingConvertTemp, readingFilteredTemp
	}

	public ViewInputStream(InputStream in) {
		super(in);
		JFrame f = new JFrame("ViewInputStream");
		f.setBounds(100, 100, 225, 300);
		psp = new PrintStreamPanel();
		f.getContentPane().add(psp);
		f.setVisible(true);
		ps = psp.getPrintStream();
	}

	public int read() throws IOException {
		int x = super.read();
		String hex = Integer.toHexString(x);
		ps.println(hex);
		return x;

	}

}
