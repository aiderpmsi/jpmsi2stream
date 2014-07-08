package com.github.aiderpmsi.pims.parser.linestypes;

public class Segment implements CharSequence {

	public char[] sequence;
	
	public int start, count;
	
	public Segment(char[] sequence, int start, int count) {
		this.sequence = sequence;
		this.start = start;
		this.count = count;
	}

	@Override
	public char charAt(int index) {
		return sequence[index + start];
	}

	@Override
	public int length() {
		return count;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return new Segment(sequence, this.start + start, this.start + end);
	}
	
	@Override
	public String toString() {
		return new String(sequence, start, count);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
            return true;
        } else if (obj instanceof Segment) {
        	final Segment segt = (Segment) obj;
        	if (count != segt.count)
        		return false;
        	else {
        		for (int i = 0 ; i < this.count ; i++) {
        			if (sequence[i + start] != segt.sequence[i + segt.start])
        				return false;
        		}
        		return true;
        	}
        } else {
        	return false;
        }
	}
}
