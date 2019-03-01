package org.eclipse.wb.swt;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Quantizer {
	private String Path = "";
	
	private int lvl;
	
	public void set_path(String p) {
		Path = p;
	}
	public String get_path() {
		return Path;
	}
	
	public void set_level(String l) {
		lvl = Integer.parseInt(l);
	}
	public int get_level() {
		return lvl;
	}
	
	public String read () { // Reads a file 
		File file = new File (Path);
		String Data = "";
		try {
		Scanner read = new Scanner(file);
		read.useDelimiter("\\Z"); // keep reading until eof
		Data = read.next();
		read.close();
		}
		
		catch (IOException  exp) {
			exp.printStackTrace();
		}
		return Data;
	}
	public void Compress() throws IOException {
		
		int max = 0, min = 300; 
		
		double avg = 0;
		File file = new File(Path);
	    BufferedImage img = null;
		
	    img = ImageIO.read(file);
	    int width = img.getWidth();
	    int height = img.getHeight();
	    
	    int[][] imgArr = new int[width][height];
	    Raster raster = img.getData(); 
	    
	    for (int i = 0; i < width; i++) {
	        for (int j = 0; j < height; j++) {
	            imgArr[i][j] = raster.getSample(i, j, 0);
	            avg += imgArr[i][j];
	            
	            if(imgArr[i][j] > max)
	            	max = imgArr[i][j];
	            
	            if(imgArr[i][j] < min)
	            	min = imgArr[i][j]; 
	        }
	    }
	    avg /=  (width * height);
	    //System.out.println(max + "  " + min + "  " + avg + "  " + lvl);
	    
	    ArrayList<Integer>arr = new ArrayList<Integer>();
	    ArrayList<Double>avgs = new ArrayList<Double>();
	    
	    	
	    int it = 1, arridx = 0, avgidx = 0;
	    
	    int u = (int) Math.ceil(avg), l = (int) Math.floor(avg);
	    if ((int) avg == avg) {
	    	u = (int) (avg + 1);
	    	l = u - 1;
	    }
	    if (u == l)
	    	l = u - 1;
	    arr.add(u);
	    arr.add(l);
	
	    while(arr.size() < lvl + 2) {
	    	// for each avg get a 2 new numbers
	    	if (it % 2 == 0) {
	    		for(int x = avgidx; x < avgs.size(); x++) {
	    			int up = (int) Math.ceil(avgs.get(x)), lo = (int) Math.floor(avgs.get(x));
		    		if (up == lo)
		    			lo = up - 1;
		    		arr.add(up);
		    		arr.add(lo);
	    		}
	    		avgidx += (avgs.size() - avgidx);
	    	}
	    		// for each new number get the new avg 
	    	else {
	    		for(int x = arridx; x < arr.size(); x++) {
	    			double sum = 0; 
	    			int num = 0; 
	    			
	    			for (int i = 0; i < width; i++) {
	    		        for (int j = 0; j < height; j++) {
	    		        	boolean add = true;
	    		        	for(int y = arridx; y < arr.size(); y++) {
	    		        		if(Math.abs(imgArr[i][j] - arr.get(y)) < Math.abs(imgArr[i][j] - arr.get(x))) {
	    		        			add = false;
	    		        			break;
	    		        		}
	    		        	}
	    		        	if(add) {
	    		        		sum += imgArr[i][j];
	    		        		num++;
	    		        	}
	    		        }
	    		
	    			}
	    			sum = sum / num;
	    			avgs.add(sum);
	    			
	    		}
	    		arridx += (arr.size() - arridx);
	    	}
	    	it++;
	    }
	    // get the left avg for the last numbers
	    for(int x = arr.size() - lvl; x < arr.size(); x++) {
			double sum = 0; 
			int num = 0; 
			
			for (int i = 0; i < width; i++) {
		        for (int j = 0; j < height; j++) {
		        	boolean add = true;
		        	for(int y = arridx; y < arr.size(); y++) {
		        		if(Math.abs(imgArr[i][j] - arr.get(y)) < Math.abs(imgArr[i][j] - arr.get(x))) {
		        			add = false;
		        			break;
		        		}
		        	}
		        	if(add) {
		        		sum += imgArr[i][j];
		        		num++;
		        	}
		        }
		
			}
			sum = sum / num;
			avgs.add(sum);
	    }
	   
	    int q_1[] = new int [lvl];
	    int idx = 0;
	    for (int i = avgs.size() - 1; i > avgs.size() - lvl - 1; i--) {
	    	q_1[idx] = (int) Math.ceil(avgs.get(i));
	    	idx++;
	    }
	    
	    int q [] = new int [lvl];
	    for(int i = 0; i < lvl; i++)
	    	q[i] = i;
	    
	    ArrayList<Double> r = new ArrayList<>(); 
	    double key = 0;
	    
	    for(int i = 0; i < lvl; i++) {
	    	if(i == lvl - 1)
	    		break;
	    	double value = (q_1[i] + q_1[i + 1]) / 2;
	    	r.add(key);
	    	r.add(value);
	    	key = value;
	    }
	    r.add(key);
	    r.add((double) (max+1));
	    
	    
	    Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("C:\\Users\\amr_x\\Desktop\\Compressed.txt"), "utf-8"));
	    
	    out.write(String.valueOf(width));
	    out.write('-');
	    out.write(String.valueOf(height));
	    out.write('#');
	    System.out.println(width + "  "+ height);
	    
	    
	    for(int i = 0; i < lvl; i++) {
	    	if(i != lvl - 1) {
	    		out.write(String.valueOf(q[i]));
	    		out.write('-');
	    	}
	    	else {
	    		out.write(String.valueOf(q[i]));
	    	}
	    	System.out.print(q[i] + "  ");
	    }
	    System.out.println();
	    out.write('#');
	    
	    for(int i = 0; i < lvl; i++) {
	    	if(i != lvl - 1) {
	    		out.write(String.valueOf(q_1[i]));
	    		out.write('-');
	    	}
	    	else {
	    		out.write(String.valueOf(q_1[i]));
	    	}
	    	System.out.print(q_1[i] + "  ");
	    }
	    System.out.println();
	    out.write('#');
	    
	    for (int i = 0; i <width; i++) {
		    for(int j = 0; j < height; j++ ) {
		    	int idx1 = 0;
		    	for(int x = 0; x < lvl * 2; x++) {
		    		if (imgArr[i][j] >= r.get(x) &&  imgArr[i][j] < r.get(x+1)) {
		    			out.write(String.valueOf(q[idx1]));
		    			break;
		    		}
		    		else {
		    			idx1++;
		    			x++;
		    		}
		    	}
		    }
	    }
	    	    
	    out.close();
	    
	    
	    
	    }
	
	
	
	public void Decompress()throws IOException {
	
		String data = read();
		String q = "";
		String w = "";
		String h = "";
		String q_1 = "";
		String t = "";
		
		int idx = data.indexOf('-');
		w = data.substring(0, idx);
		int width = Integer.parseInt(w);
		
		data = data.substring(idx + 1);
		idx = data.indexOf('#');
		
		h = data.substring(0, idx);
		int height = Integer.parseInt(h);
		
		data = data.substring(idx + 1);
		
		idx = data.indexOf('#');
		
		
		q = data.substring(0, idx);
		data = data.substring(idx + 1);
		
		idx = data.indexOf('#');
		q_1 = data.substring(0, idx);
		
		data = data.substring(idx + 1);
		
		int temp [][] = new int [width][height];
	
		
		
		ArrayList<String> arr = new ArrayList<String>();
		t = "";
		idx = 0;
		
		for(int i = 0; i < q.length(); i++) {
			if(q.charAt(i) != '-')
				t+= q.charAt(i);
			else {
				arr.add(t);
				t = "";
			}
		}
		arr.add(t);

		ArrayList<String> arr1 = new ArrayList<String>();
		t = "";
		idx = 0;
		
		for(int i = 0; i < q_1.length(); i++) {
			if(q_1.charAt(i) != '-')
				t+= q_1.charAt(i);
			else {
				arr1.add(t);
				t = "";
			}
		}
		arr1.add(t);
		
		int x = 0, y = 0;
		int mat[][] = new int [width][height];
		
		for(int i = 0; i < data.length(); i++) {
			String code = data.charAt(i)+"";
			int index = arr.indexOf(code);
			
			mat[x][y++] = Integer.parseInt(arr1.get(index));
			
			if(y == height) {
				y = 0;
				x++;		
			}
		
		}
		
		
		ReadWriteImage obj = new ReadWriteImage();
		obj.writeImage(mat, "C:\\Users\\amr_x\\Desktop\\Decompressed.jpg");
		
	}
}
