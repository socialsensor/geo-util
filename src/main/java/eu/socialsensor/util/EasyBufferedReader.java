package eu.socialsensor.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class EasyBufferedReader extends BufferedReader {

	
	static final Reader createReader(String textFile){
		try {
			return new InputStreamReader(new FileInputStream(textFile), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public EasyBufferedReader(String textFile) {
		super(createReader(textFile));
	}

	@Override
	public void close() {
		try {
			super.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String readLine() {
		try {
			return super.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
