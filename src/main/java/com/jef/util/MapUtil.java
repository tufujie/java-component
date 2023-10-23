package com.jef.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MapUtil implements Map<String, String> {

	private final String name;
	private final Map<String, String> props;

	@SuppressWarnings("unchecked")
	public MapUtil(String name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		this.name = name;
		this.props = new LinkedHashMap();
	}

	private static boolean isKeyValueSeparatorChar(char c) {
		return (Character.isWhitespace(c)) || (c == ':') || (c == '=');
	}

	private static boolean isCharEscaped(CharSequence s, int index) {
		return (index > 0) && (s.charAt(index - 1) == '\\');
	}

	protected static String[] splitKeyValue(String keyValueLine) {
		String line = StringUtils.clean(keyValueLine);
		if (line == null) {
			return null;
		}
		StringBuilder keyBuffer = new StringBuilder();
		StringBuilder valueBuffer = new StringBuilder();

		boolean buildingKey = true;

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			if (buildingKey) {
				if ((isKeyValueSeparatorChar(c)) && (!isCharEscaped(line, i))) {
					buildingKey = false;
				} else {
					keyBuffer.append(c);
				}
			} else {
				if ((valueBuffer.length() == 0) && (isKeyValueSeparatorChar(c)) && (!isCharEscaped(line, i))) {
					continue;
				}
				valueBuffer.append(c);
			}

		}

		String key = StringUtils.clean(keyBuffer.toString());
		String value = StringUtils.clean(valueBuffer.toString());

		if ((key == null) || (value == null)) {
			String msg = "Line argument must contain a key and a value.  Only one string token was found.";
			throw new IllegalArgumentException(msg);
		}

		return new String[]{key, value};
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void clear() {
		this.props.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.props.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.props.containsValue(value);
	}

	@Override
	public Set<Entry<String, String>> entrySet() {
		return this.props.entrySet();
	}

	@Override
	public String get(Object key) {
		return this.props.get(key);
	}

	@Override
	public boolean isEmpty() {
		return this.props.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return this.props.keySet();
	}

	@Override
	public String put(String key, String value) {
		return this.props.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		this.props.putAll(m);
	}

	@Override
	public String remove(Object key) {
		return this.props.remove(key);
	}

	@Override
	public int size() {
		return this.props.size();
	}

	@Override
	public Collection<String> values() {
		return this.props.values();
	}

	@Override
	public String toString() {
		@SuppressWarnings("hiding")
		String name = getName();
		if ("".equals(name)) {
			return "<default>";
		}

		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof MapUtil)) {
			MapUtil other = (MapUtil) obj;
			return (getName().equals(other.getName())) && (this.props.equals(other.props));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (this.name.hashCode() * 31) + this.props.hashCode();
	}

}
