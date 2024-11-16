package sk.upjs.gursky.pdb;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnclusteredBPTreeSalaryTest {

	private UnclusteredBPTreeSalary bptree;

    @Before
    public void setUp() throws Exception {
        bptree = UnclusteredBPTreeSalary.createByBulkLoading();
    }

    @After
    public void tearDown() throws Exception {
        bptree.close();
        UnclusteredBPTreeSalary.INDEX_FILE.delete();
    }
	@Test
	public void test() throws IOException {
		long time = System.nanoTime();
        List<PersonEntry> result = bptree.unclusteredSalaryIntervalQuery(new SalaryKey(400), new SalaryKey(3000));
        time = System.nanoTime() - time;

        System.out.println("Interval uncluseteredSalary: " + time/1_000_000.0 +" ms");
        for (var e : result) {
            assertTrue(e.salary >= 400 && e.salary <= 3000);
        }
	}

}
