package junit.activiti;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestActiviti {

	ApplicationContext ioc = new ClassPathXmlApplicationContext("spring/spring-*.xml");
	ProcessEngine processEngine = (ProcessEngine)ioc.getBean("processEngine");
	
	@Test
	public void test06(){
		ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
		
		HistoryService historyService = processEngine.getHistoryService();
		
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
	
		HistoricProcessInstance historicProcessInstance = historicProcessInstanceQuery.processInstanceId("303").finished().singleResult();
		
		System.out.println(historicProcessInstance);
	}
	
	
	@Test
	public void test05(){
		ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
		
		TaskService taskService = processEngine.getTaskService();
		
		TaskQuery createTaskQuery = taskService.createTaskQuery();
		
		List<Task> list1 = createTaskQuery.taskAssignee("zhangsan").list();
		
		
		for(Task task : list1){
			System.out.println("id="+task.getId());
		}
		
	}
	
	
	
	/**
	 * act_hi_actinst,历史的活动的任务表
	 * act_hi_procinst,历史的流程实例表 ，一启动一个流程，这个表就有一部分数据
	 * act_hi_taskinst,历史的流程任务表
	 * act_ru_execution,正在运行的任务表
	 * act_ru_task,运行的任务数据表
	 */
	
	@Test
	public void test04(){
		System.out.println("processEngine="+processEngine);
	
		ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().singleResult();
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
		
		System.out.println("processInstance:"+processInstance);
		
	}
	@Test
	public void test02(){
		System.out.println("processEngine="+processEngine);
		
		RepositoryService repositoryService = processEngine.getRepositoryService();
		
		Deployment deploy = repositoryService.createDeployment().addClasspathResource("MyProcess2.bpmn").deploy();
		
		System.out.println(deploy);
	}
	
	@Test
	public void test03(){
		System.out.println(processEngine);
		
		RepositoryService repositoryService = processEngine.getRepositoryService();
		
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		
		List<ProcessDefinition> list = processDefinitionQuery.list();
		
		for(ProcessDefinition processDefinition : list) {
			System.out.println("Id=" + processDefinition.getId());
			System.out.println("Key=" + processDefinition.getKey());
			System.out.println("Name=" + processDefinition.getName());
			System.out.println("Version=" + processDefinition.getVersion());
		}
		
		
		ProcessDefinition singleResult = processDefinitionQuery.latestVersion().singleResult();//获取最后一次部署的流程定义对象
		System.out.println("Id=" + singleResult.getId());
		System.out.println("Key=" + singleResult.getKey());
		System.out.println("Name=" + singleResult.getName());
		System.out.println("Version=" + singleResult.getVersion());
		//排序查询流程定义，分页查询流程定义
		ProcessDefinitionQuery definitionQuery = processDefinitionQuery.orderByProcessDefinitionVersion().asc();
		List<ProcessDefinition> listPage = definitionQuery.listPage(0, 2);//这个查询的是每个流程的最大值，不是流程的所有版本都查询出来
		//根据流程定义的key，查询流程定义对象
		ProcessDefinition query = processDefinitionQuery.processDefinitionKey("myProcess").latestVersion().singleResult();
	}
	
	
	//创建流程引擎，创建23张表
	@Test
	public void test01(){
		
		System.out.println("processEngine:" + processEngine);
	}
	
	
}
