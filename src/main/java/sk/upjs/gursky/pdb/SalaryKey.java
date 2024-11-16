package sk.upjs.gursky.pdb;

import java.nio.ByteBuffer;

import sk.upjs.gursky.bplustree.BPKey;

public class SalaryKey implements BPKey<SalaryKey> {

	private static final long serialVersionUID = 1618843651920973765L;
	private int key;
	
	public SalaryKey() {
	}

	public SalaryKey(int key) {
		this.key = key;
	}

	@Override
	public int compareTo(SalaryKey o) {
		return Integer.compare(this.key, o.key);
	}

	@Override
	public void load(ByteBuffer bb) {
		bb.getInt();

	}

	@Override
	public void save(ByteBuffer bb) {
		bb.putInt(key);

	}

	@Override
	public int getSize() {
		return 4;
	}

}
