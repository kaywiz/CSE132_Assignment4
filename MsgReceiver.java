package assignment4;

import studio4.SerialComm;

import java.io.*;

import assignment4.ViewInputStream.State;

public class MsgReceiver {

	final private ViewInputStream vis;
	
	public MsgReceiver(InputStream in) {
		vis = new ViewInputStream(in);
	}
	
	public void run() throws IOException {

	        

			State state = State.waitingForMagicNumber;
			
			int byte1 = 0;
			
			while (true) {
				
					//State nextState = null;
					
					switch (state) {
					case waitingForMagicNumber:
						if (vis.read() == 33) {
							state = State.waitingForInputType;
						} //else {
							//state = State.waitingForMagicNumber;
						//}
						break;
					case waitingForInputType: //how do i account for reading the magic symbol first?
						byte1 = vis.read();
						String type = "";
						switch (byte1) {
						case 48:
							System.out.println("input");
							type = "Debugging String";
							state = State.readingDebugString;
						case 49:
							type = "Error String";
							state = State.readingErrorString;
						case 50:
							type = "Timestamp";
							state = State.readingTimestamp;
						case 51:
							type = "Poten";
							state = State.readingPoten;
						case  52:
							type = "Raw Temp";
							state = State.readingRawTemp;
						case 53:
							type = "Convert Temp";
							state = State.readingConvertTemp;
						case 54:
							type = "Filtered Temp";
							state = State.readingFilteredTemp;
						default:
							//type = "Potentiometer Bad!";
							state = State.waitingForMagicNumber;
							break;
						}
						System.out.println("Input type: "+type);
						break;
						
					case readingDebugString:
						char add = ' ';
						String receivedString = "";
						int x = vis.read();
						while(((x < 123) && (x > 96)) || ((x < 91) && (x > 64)) || (x == 32)){
							add = (char)x;
							receivedString = receivedString + add;
							x = vis.read();
						}
						//DataInputStream dis = new DataInputStream(vis);
						//String receivedString = dis.readUTF();
						state = State.waitingForInputType;
						System.out.println("Debug String: "+receivedString);
						break;
					case readingErrorString:
						char plus = ' ';
						String erString = "";
						int inputError = vis.read();
						while(((inputError < 123) && (inputError > 96)) || ((inputError < 91) && (inputError > 64)) || (inputError == 32)){
							plus = (char)inputError;
							erString = erString + plus;
							inputError = vis.read();
						}
						//DataInputStream dis1 = new DataInputStream(vis);
						//String receivedString2 = dis1.readUTF();
						state = State.waitingForInputType;
						System.out.println("Error String: "+ erString);
						break;
					case readingTimestamp:
						int t3 = vis.read();
						int t2 = vis.read();
						int t1 = vis.read();
						int t = vis.read();
						t3 = t3 << 24;
						t2 = t2 << 16;
						t1 = t1 << 8 ;
						int totalT = t3 + t2 + t1 + t;
						//DataInputStream dis2 = new DataInputStream(vis);
						//int receivedInt1 = dis2.readInt();
						//String timestamp = Integer.toString(receivedInt1);
						state = State.waitingForMagicNumber;
						System.out.println("Timestamp: " + totalT + "milliseconds");
						break;
					case readingPoten:
						int poten = vis.read();
						int poten1 = vis.read();
						poten = poten << 8;
						int totalPoten = poten + poten1;
						//DataInputStream dis3 = new DataInputStream(vis);
						//int receivedInt2 = dis3.readInt();
						//String poten = Integer.toString(receivedInt2);
						state = State.waitingForMagicNumber;
						System.out.println("Potentiometer: " + totalPoten);
						break;
					case readingRawTemp:
						int rawTemp = vis.read();
						int rawTemp1 = vis.read();
						rawTemp = rawTemp << 8;
						int temp = rawTemp + rawTemp1;
						//DataInputStream dis4 = new DataInputStream(vis);
						//int receivedInt3 = dis4.readInt();
						//String temp = Integer.toString(receivedInt3);
						System.out.println("Raw Temp: " + temp);
						state = State.waitingForMagicNumber;
						break;
					case readingConvertTemp:
						int convertT3 = vis.read();
						int convertT2 = vis.read();
						int convertT1 = vis.read();
						int convertT = vis.read();
						convertT3 = convertT3 << 24;
						convertT2 = convertT2 << 16;
						convertT1 = convertT1 << 8;
						int convertTemp = convertT3 + convertT2 + convertT1 + convertT;
						//DataInputStream dis5 = new DataInputStream(vis);
						//int receivedInt4 = dis5.readInt();
						//String convertTemp = Integer.toString(receivedInt4);
						System.out.println("Converted Temp: " + convertTemp);
						state = State.waitingForMagicNumber;
						break;
					case readingFilteredTemp:
						int filteredT3 = vis.readInt();
						int filteredT2 = vis.readInt();
						int filteredT1 = vis.read();
						int filteredT = vis.read();
						filteredT3 = filteredT3 << 24;
						filteredT2 = filteredT2 << 16;
						filteredT1 = filteredT1 << 8;
						int filterTemp = filteredT3 + filteredT2 + filteredT1 + filteredT;
						//DataInputStream dis6 = new DataInputStream(vis);
						//int receivedInt5 = dis6.readInt();
						//String filterTemp = Integer.toString(receivedInt5);
						System.out.println("Filtered Temp: " + filterTemp);
						state = State.waitingForMagicNumber;
						break;
					default:
						state = State.waitingForMagicNumber;
						System.out.println("error");
						break;
						
					}
				}
				
			}
		
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        try
        {        	
            SerialComm s = new SerialComm();
            s.connect("/dev/cu.usbserial-DN00MZW8"); // Adjust this to be the right port for your machine
            InputStream in = s.getInputStream();
            MsgReceiver msgr = new MsgReceiver(in);
            msgr.run();
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

	}

}
