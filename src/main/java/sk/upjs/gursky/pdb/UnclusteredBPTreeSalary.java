package sk.upjs.gursky.pdb;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import sk.upjs.gursky.bplustree.BPTree;

public class UnclusteredBPTreeSalary extends BPTree<SalaryKey, SalaryOffsetEntry> {

	private static final long serialVersionUID = -3849289387248166339L;
	public static final File INDEX_FILE = new File("person.unclustered.tree");
	public static final File INPUT_DATA_FILE = new File("person.tab");

	public UnclusteredBPTreeSalary() {
		super(SalaryOffsetEntry.class, INDEX_FILE);
	}

	public static UnclusteredBPTreeSalary createByBulkLoading() throws IOException {
		long startTime = System.nanoTime();
		UnclusteredBPTreeSalary tree = new UnclusteredBPTreeSalary();
		RandomAccessFile raf = new RandomAccessFile(INPUT_DATA_FILE, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(4096); // velkost stranky
		List<SalaryOffsetEntry> entries = new ArrayList<SalaryOffsetEntry>();

		long fileSize = INPUT_DATA_FILE.length();
		for (int offset = 0; offset < fileSize; offset += 4096) {
			System.out.println("processing page " + (offset / 4096));
			buffer.clear();
			channel.read(buffer, offset);
			buffer.rewind();
			int numberOfRecords = buffer.getInt(); // precita si 4 bajty z buffra, sipka sa tam prevynie
			for (int i = 0; i < numberOfRecords; i++) {
				PersonEntry entry = new PersonEntry();
				entry.load(buffer);
				long entryOffset = offset + 4 + i * entry.getSize();
				SalaryOffsetEntry item = new SalaryOffsetEntry(entry.salary, entryOffset);
				entries.add(item);
			}
		}
		Collections.sort(entries);
		tree.openAndBatchUpdate(entries.iterator(), entries.size());
		channel.close();
		raf.close();
		System.out.println("Unclustered index created in " + (System.nanoTime() - startTime) / 1_000_000.0 + " ms");
		return tree;
	}
	
	public List<PersonEntry> unclusteredSalaryIntervalQuery(SalaryKey low, SalaryKey high) throws IOException{
		List<SalaryOffsetEntry> references = intervalQuery(low, high);

		RandomAccessFile raf = new RandomAccessFile(INPUT_DATA_FILE, "r");

		FileChannel channel = raf.getChannel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(4096); 
		List<PersonEntry> result = new LinkedList<PersonEntry>();
		long lastOffset = -1L;
		int accesses = 0;
		for (SalaryOffsetEntry ref : references) {
			long pageOffset = (ref.offset / 4096) * 4096; 
			if (lastOffset != pageOffset) {
				lastOffset = pageOffset;
				buffer.clear();
				channel.read(buffer, pageOffset);
				accesses++;
			}
			
			int entryInPageOffset = (int) (ref.offset - pageOffset);
			buffer.position(entryInPageOffset);
			PersonEntry entry = new PersonEntry();
			entry.load(buffer);
			result.add(entry);
			
		}
		
		channel.close();
		raf.close();
		System.out.println("I/O operations: " + accesses);
		return result;
		
	}

}
