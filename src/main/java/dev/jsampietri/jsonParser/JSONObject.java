package dev.jsampietri.jsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import dev.jsampietri.jsonParser.exception.JSONObjectException;

public class JSONObject {
	private JSONObjectType type;
	private Object value;
	
	public JSONObject()
	{
		
	}

	public JSONObject(JSONObjectType type) {
		super();
		this.type = type;
	}

	public JSONObject(JSONObjectType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	public JSONObjectType getType() {
		return type;
	}

	public void setType(JSONObjectType type) {
		this.type = type;
	}

	public String getString() throws JSONObjectException{
		if (this.type != JSONObjectType.STRING)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		return (String) this.value;
	}

	public void setValue(String value) throws JSONObjectException{
		if (this.type != JSONObjectType.STRING)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		this.value = value;
	}

	public Double getDouble() throws JSONObjectException{
		if (this.type != JSONObjectType.DOUBLE)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		return (Double) this.value;
	}

	public void setValue(Double value) throws JSONObjectException{
		if (this.type != JSONObjectType.DOUBLE)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		this.value = value;
	}

	public Long getLong() throws JSONObjectException{
		if (this.type != JSONObjectType.LONG)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		return (Long) this.value;
	}

	public void setValue(Long value) throws JSONObjectException{
		if (this.type != JSONObjectType.LONG)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		this.value = value;
	}

	public Boolean getBoolean() throws JSONObjectException{
		if (this.type != JSONObjectType.BOOLEAN)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		return (Boolean) this.value;
	}

	public void setValue(Boolean value) throws JSONObjectException{
		if (this.type != JSONObjectType.BOOLEAN)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, JSONObject> getMapValues() throws JSONObjectException{
		if (this.type != JSONObjectType.OBJECT)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		return (HashMap<String, JSONObject>) this.value;
	}

	public void setValue(HashMap<String, JSONObject> value) throws JSONObjectException{
		if (this.type != JSONObjectType.OBJECT)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<JSONObject> getArray() throws JSONObjectException{
		if (this.type != JSONObjectType.ARRAY)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		return (ArrayList<JSONObject>) this.value;
	}

	public void setValue(ArrayList<JSONObject> value) throws JSONObjectException{
		if (this.type != JSONObjectType.ARRAY)
		{
			throw new JSONObjectException("The value is not compatible with the type");
		}
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	public void print() {
		switch (this.type) {
		case OBJECT:
			System.out.print("{");
			int i = 0;
			HashMap<String, JSONObject> content = (HashMap<String, JSONObject>)this.value;
			Set<Entry<String,JSONObject>> entrySet = content.entrySet();
			for (Entry<String, JSONObject> e : entrySet) {
				System.out.print("\"" + e.getKey() + "\":");
				((JSONObject)e.getValue()).print();
				i++;
				if(i < entrySet.size())
					System.out.println(",");
			}
			System.out.print("}");
			break;

		case ARRAY:
			System.out.print("[");
			int j = 0;
			ArrayList<JSONObject> listValues = ((ArrayList<JSONObject>)this.value);
			for (JSONObject o : listValues) {
				o.print();
				j++;
				if(j < listValues.size())
					System.out.println(",");
			}
			System.out.print("]");
			break;

		case STRING:
			System.out.print("\"");
			System.out.print(this.value);
			System.out.print("\"");
			break;

		case DOUBLE:
			System.out.print(this.value);
			break;

		case LONG:
			System.out.print(this.value);
			break;

		case BOOLEAN:
			System.out.print(this.value);
			break;

		case NULL:
			System.out.print("null");
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getValueByPath(String path) throws JSONObjectException
	{
		ArrayList<Object> result = new ArrayList<Object>();
		switch (this.type) {
		case OBJECT:
			if (!path.isEmpty()) {
				ArrayList<String> sPath = new ArrayList<String>(Arrays.asList(path.split("\\.")));
				String key = sPath.get(0);
				HashMap<String, JSONObject> content = (HashMap<String, JSONObject>) this.value; 
				if (content.containsKey(key)) {
					String nPath = "";
					if (sPath.size() > 0) {
						sPath.remove(0);
						nPath = String.join(",", sPath);
					}
					result = content.get(key).getValueByPath(nPath);
				}
			} else {
				result.add(this);
			}
			break;
		case ARRAY:
			List<JSONObject> content = (List<JSONObject>) this.value;
			for (JSONObject o : content) {
				result.addAll((List<Object>) o.getValueByPath(path));
			}
			break;
		case STRING:
			result.add((String) this.value);
			break;
		case DOUBLE:
			result.add((Double) this.value);
			break;
		case LONG:
			result.add((Long) this.value);
			break;
		case BOOLEAN:
			result.add((Boolean) this.value);
			break;
		case NULL:
			break;
		}
		return result;
	}
}
