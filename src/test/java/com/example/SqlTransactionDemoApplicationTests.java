package com.example;

import com.example.model.Employee;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class SqlTransactionDemoApplicationTests {

	private static final int SLEEP_IN_MILLIS = 20;
	
	@SpyBean
	private EmployeeService employeeService;

	private CyclicBarrier cyclicBarrier;

	private Employee employee;

	@BeforeEach
	public void setup() {
		cyclicBarrier = new CyclicBarrier(2);

		System.out.println("setup");
		Employee temp = new Employee();
		temp.setName("John");
		employee = employeeService.save(temp);
	}

	@Test
	void alwaysWorks() throws InterruptedException {
		Mockito.doAnswer(invocation -> {
			Object result = invocation.callRealMethod();
			cyclicBarrier.await();
			return result;
		}).when(employeeService).getVersion(any());


		Thread thread1 = new Thread(() -> {
			employeeService.update(employee.getId(), "John1");
		});

		Thread thread2 = new Thread(() -> {
			employeeService.update(employee.getId(), "John2");
		});

		thread1.start();
		thread2.start();

		Thread.sleep(SLEEP_IN_MILLIS);
		thread1.join();
		thread2.join();

		Assertions.assertEquals(1, employeeService.get(employee.getId()).getVersion(), "version should be 1");

	}

	@Test
	void sometimesDoesNotWork1() throws InterruptedException {

		Thread thread1 = new Thread(() -> {
			barrierAwait(cyclicBarrier);
			employeeService.update(employee.getId(), "John1");
		});

		Thread thread2 = new Thread(() -> {
			barrierAwait(cyclicBarrier);
			employeeService.update(employee.getId(), "John2");
		});

		thread1.start();
		thread2.start();

		Thread.sleep(SLEEP_IN_MILLIS);
		thread1.join();
		thread2.join();

		Assertions.assertEquals(1, employeeService.get(employee.getId()).getVersion(), "version should be 1");

	}

	private static void barrierAwait(CyclicBarrier cyclicBarrier) {
		try {
			cyclicBarrier.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (BrokenBarrierException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void sometimesDoesNotWork2() throws InterruptedException {

		Mockito.doAnswer(invocation -> {
			cyclicBarrier.await();
			Object result = invocation.callRealMethod();
			return result;
		}).when(employeeService).getVersion(any());

		Thread thread1 = new Thread(() -> {
			employeeService.update(employee.getId(), "John1");
		});

		Thread thread2 = new Thread(() -> {
			employeeService.update(employee.getId(), "John2");
		});

		thread1.start();
		thread2.start();

		Thread.sleep(SLEEP_IN_MILLIS);
		thread1.join();
		thread2.join();

		Assertions.assertEquals(1, employeeService.get(employee.getId()).getVersion(), "version should be 1");

	}

}
