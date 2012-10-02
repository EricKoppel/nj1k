package utils;

import java.util.Comparator;
import java.util.TreeSet;

public class FixedSizeTreeSet<T> extends TreeSet<T> {

	private static final long serialVersionUID = 1L;
	private long maxSize;

	public FixedSizeTreeSet(long maxSize, Comparator<T> c) {
		super(c);
		this.maxSize = maxSize;
	}
	
	@Override
	public boolean add(T e) {
		boolean duplicate = super.add(e);
		
		if (this.size() > maxSize) {
			this.remove(this.last());
		}
		
		return duplicate;
	}
}
