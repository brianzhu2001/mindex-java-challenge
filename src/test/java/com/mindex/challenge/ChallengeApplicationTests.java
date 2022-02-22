package com.mindex.challenge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChallengeApplicationTests {

	/**
	 * No Java unit tests. Testing was done using Postman.
	 * 
	 * ReportingStructure tests were as follows:
	 * 		Query ReportingStructure for all Employees, confirm correctness.
	 * 		Add child to Pete Best, repeat.
	 * 		Add parent to John Lennon, repeat.
	 * 		Attempt to query nonexistent employeeId, confirm error. 		
	 * 	
	 * Compensation tests were as follows:
	 * 		Add a Compensation to John Lennon with a past effective date, query, confirm correctness.
	 * 		Add 3 Compensations to Paul McCartney with different effective dates, confirm the most recent is returned.
	 * 		Add another Compensation to PM with a future effective date, confirm it is not returned.
	 * 		Attempt to add Compensation to nonexistent Employee, confirm error.
	 * 		Attempt to query nonexistent employeeId or employeeId without current Compensations, confirm error.
	 * 
	 * All test cases passed.
	 */

	@Test
	public void contextLoads() {
	}

}
