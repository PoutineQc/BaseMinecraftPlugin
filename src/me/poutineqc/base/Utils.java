package me.poutineqc.base;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static List<String> getList(Object object) {
		List<String> result = new ArrayList<String>();
	    for(int i = 0; i < ((List<?>)object).size(); i++){
	    	Object item = ((List<?>) object).get(i);
	    	if(item instanceof String)
	    		result.add((String) item);
	    }
	    
	    return result;
	}
}
