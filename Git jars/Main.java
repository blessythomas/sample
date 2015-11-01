package src.main.java;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		//ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:META-INF/spring/context.xml");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("src/main/resources/context.xml");
		Job job = (Job) ctx.getBean("springBatchCsvToDbJob");
		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		try{
			JobExecution execution = jobLauncher.run(job, new JobParameters());
			System.out.println(execution.getStatus());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
