package sk.folki.aspectlogger;

import java.util.LinkedHashMap;
import java.util.Map;

class Parameters {
	private Map<String, Object> parameters = new LinkedHashMap<String, Object>();

	public void addParameter(String name, Object value) {
		parameters.put(name, value);
	}

	public String[] getNames() {
		return parameters.keySet().toArray(new String[parameters.size()]);
	}
	
	public Object getValue(String parameterName) {
		return parameters.get(parameterName);
	}
}