package src.main.java;

import java.util.List;

public class CabRoster {

	public static void main(String[] args) {
		CabRosterInterface cabRoster = new CabRosterImpl();
		//String inputFilePath = "./src/EmployeeList.xlsx";
		String inputFilePath = "./src/EmployeeDetails.xls";
		List<Employee> empList = cabRoster.readInputFile(inputFilePath);
		cabRoster.findBestShortestRoute(empList);
		cabRoster.findNoOfCabsRequired(empList);

	}
}

package src.main.java;

import java.util.List;

public interface CabRosterInterface {
	public List<Employee> readInputFile(String inputFile);
	public void findBestShortestRoute(List<Employee> empList);
	public int findNoOfCabsRequired(List<Employee> empList);
}


package src.main.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import com.aspose.cells.Cell;
import com.aspose.cells.Row;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

public class CabRosterImpl implements CabRosterInterface {//Program to interface
	
	@Override
	public List<Employee> readInputFile(String inputFile) {
		List<Employee> empList = new ArrayList<Employee>();
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					inputFile));
			Workbook workBook = new Workbook(fileInputStream);
			Worksheet workSheet = workBook.getWorksheets().get(0);
			int dataRow = 1;
			while (dataRow <= workSheet.getCells().getMaxDataRow()) {
				Row row =  workSheet.getCells().getRows().get(dataRow);
				Employee emp = new Employee();
				for (int i = 0; i < 4; i++) {
					Cell cell = row.get(i);
					switch (i) {
					case 0:
						emp.setName(cell.getStringValue());
						break;
					case 1:
						emp.setPickUp(cell.getStringValue());
						break;
					case 2:
						emp.setDrop(cell.getStringValue());
						break;
					case 3:
						emp.setGender(cell.getStringValue());
						break;
					}
				}
				dataRow++;
				empList.add(emp);
			}
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empList;
	}
	
	/**
	 * Find the best shortest route(shortest route) among the Route list and 
	 * assign it to employee
	 */
	@Override
	public void findBestShortestRoute(List<Employee> empList) {
		for (Employee emp : empList) {
			if (null != emp.getPickUp()) {
				emp.setRoute(RouteEnum.findShortestRoute(emp.getPickUp()).name());
			}
		}
	}

	/**
	 * Find the total no.of cabs required for each route
	 */
	@Override
	public int findNoOfCabsRequired(List<Employee> empList) {
		Hashtable<RouteEnum, List<Employee>> routePerEmpTable = createEmpListPerRoute(empList);
		int noOfCabsReq = 0;

		for (Entry<RouteEnum, List<Employee>> entry : routePerEmpTable
				.entrySet()) {
			noOfCabsReq = process(entry.getKey(), entry.getValue(), noOfCabsReq);
		}
		System.out.println("No. of cabs required " + noOfCabsReq);
		System.out.println("The best shortest route ");
		for (RouteEnum route : routePerEmpTable.keySet()) {
			if (!routePerEmpTable.get(route).isEmpty()) {
				System.out.println(" " + route + "->"
						+ RouteEnum.getValues((RouteEnum) route).toString());
			}
		}
		return noOfCabsReq;
	}

	/**
	 * Create the employee list for each route
	 * @param empList
	 * @return
	 */
	private Hashtable<RouteEnum, List<Employee>> createEmpListPerRoute(
			List<Employee> empList) {
		Hashtable<RouteEnum, List<Employee>> routeMap = new Hashtable<RouteEnum, List<Employee>>();
		Hashtable<String, Route> routeTable = new Hashtable<String, Route>();
		for (RouteEnum route : RouteEnum.values()) {
			routeTable.put(route.name().toString(), RouteFactory.getRoute(route.name().toString()));//factory design pattern
		}
		/*routeTable.put(RouteEnum.Route1.name().toString(), new Route1());
		routeTable.put(RouteEnum.Route2.name().toString(), new Route2());
		routeTable.put(RouteEnum.Route3.name().toString(), new Route3());
		routeTable.put(RouteEnum.Route4.name().toString(), new Route4());
		routeTable.put(RouteEnum.Route5.name().toString(), new Route5());
		routeTable.put(RouteEnum.Route6.name().toString(), new Route6());
		*/
		 for (Employee emp : empList) {
			 if( null != emp.getRoute()) {//state design pattern
				 RouteContext rc = new RouteContext();
				 rc.setEmpRoute(emp, routeTable);
				 rc.createEmpListPerRoute(emp, routeMap);
				 
				/* if (emp.getRoute().equals(RouteEnum.Route1.name().toString())) {
					 route1EmpList.add(emp);
				 } else  if (emp.getRoute().equals(RouteEnum.Route2.name().toString())) {
					 route2EmpList.add(emp);
				 } else  if (emp.getRoute().equals(RouteEnum.Route3.name().toString())) {
					 route3EmpList.add(emp);
				 } else  if (emp.getRoute().equals(RouteEnum.Route4.name().toString())) {
					 route4EmpList.add(emp);
				 }*/
			 }
		 }
		return routeMap;
	}
	
	private static int process(RouteEnum route, List<Employee> emp, int noOfCabsReq) {
		int noOfPersons = 0;
		List<String> locations = Arrays.asList(route.getLocation());
		if (null != emp) {
			for (Employee e : emp) {
				if (e.getPickUp().equals(locations.get(0).split(",")[0])
						&& e.getGender().equals("Female")) {
					noOfPersons = noOfPersons + 1;
					break;
				}
			}
			noOfPersons = noOfPersons + emp.size();
			noOfCabsReq = noOfCabsReq + (int) Math.ceil((double) noOfPersons / 4);
		}
		return noOfCabsReq;
	}

}
package src.main.java;

import java.util.Hashtable;
import java.util.List;

public interface Route {
	void createEmpListPerRoute(Employee e, Hashtable<RouteEnum, List<Employee>> routeMap);

	//void setEmpRoute(Employee emp);
}

package src.main.java;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Route1 implements Route {
	List<Employee> route1EmpList;

	@Override
	public void createEmpListPerRoute(Employee e,
			Hashtable<RouteEnum, List<Employee>> routeMap) {
		if (null == routeMap.get(RouteEnum.Route1)) {
			route1EmpList = new ArrayList<Employee>();
		} else {
			route1EmpList = routeMap.get(RouteEnum.Route1);
		}
		route1EmpList.add(e);
		routeMap.put(RouteEnum.Route1, route1EmpList);
	}

}
package src.main.java;

import java.util.Hashtable;
import java.util.List;

public class RouteContext {
	private Route empRoute;
	
	public void setEmpRoute(Employee emp, Hashtable<String, Route> table) {
		empRoute = table.get(emp.getRoute());
	}

	/*public void setRoute(Employee emp, Route route) {
		empRoute = route;
	}*/
	

	public void createEmpListPerRoute(Employee emp, Hashtable<RouteEnum, List<Employee>> routeMap) {
		empRoute.createEmpListPerRoute(emp, routeMap);
	}

	/*public void assignRoute(Employee emp) {
		empRoute.setEmpRoute(emp);

	}
*/}
package src.main.java;



public enum RouteEnum {
	Route1("Electronic city, SilkBoard, Agara, Ecospace", 30),
	Route2("Indiranagar, Tippasandra, HAL, Marathhalli, Ecospace", 15), 
	Route3("RamamurtyNagar, EMC, Marathhalli, Ecospace", 30), 
	Route4("Whitefield, Sarjapur, Marathhalli, Ecospace", 25),
	Route5("Electronic city, Whitefield, Sarjapur, Marathhalli, Ecospace", 35),
	Route6("RamamurtyNagar, NGEF, HAL, Marathhalli, Ecospace", 35);

	private String location;
	private int distance;

	RouteEnum(String area, int distance) {
		this.location = area;
		this.distance = distance;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	public static StringBuilder getValues(RouteEnum route){
		StringBuilder sb = new StringBuilder();
			sb.append(route.location);
			sb.append(" ");
		return sb;
	}
	
	public static RouteEnum findShortestRoute(String pickUp){
		RouteEnum route = null;
		int dist = 0;
		RouteEnum[] str = RouteEnum.values();
		for (RouteEnum s: str) {
			if (s.location.contains(pickUp)) {
				route = s;
				dist = s.distance;
				break;
			}
		}
		for (RouteEnum s: str) {
			if (s.location.contains(pickUp) && s.distance < dist) {
				route = s;
			}
		}
		return route;
	}

}
package src.main.java;

public class RouteFactory {
	public static Route getRoute(String routeName) {
		if (routeName.equals("Route1")) {
			return new Route1();
		} else if (routeName.equals("Route2")) {
			return new Route2();
		} else if (routeName.equals("Route3")) {
			return new Route3();
		} else if (routeName.equals("Route4")) {
			return new Route4();
		} else if (routeName.equals("Route5")) {
			return new Route5();
		} else if (routeName.equals("Route6")) {
			return new Route6();
		}
		return null;
	}

}
package src.main.java;
public class Employee {
	private String name;
	private String pickUp;
	private String drop;
	private String gender;
	private String route;
	private Route empRoute;

	public Employee(String name, String pickUp, String drop,
			String gender) {
		this.name = name;
		this.pickUp = pickUp;
		this.drop = drop;
		this.gender = gender;
	}

	public Employee() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPickUp() {
		return pickUp;
	}

	public void setPickUp(String pickUp) {
		this.pickUp = pickUp;
	}

	public String getDrop() {
		return drop;
	}

	public void setDrop(String drop) {
		this.drop = drop;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public void setEmpRoute(Route empRoute) {
		this.empRoute = empRoute;
	}

}
package src.main.java;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Route2 implements Route {
	List<Employee> route2EmpList;

	@Override
	public void createEmpListPerRoute(Employee e,
			Hashtable<RouteEnum, List<Employee>> routeMap) {
		if (null == routeMap.get(RouteEnum.Route2)) {
			route2EmpList = new ArrayList<Employee>();
		} else {
			route2EmpList = routeMap.get(RouteEnum.Route2);
		}
		route2EmpList.add(e);
		routeMap.put(RouteEnum.Route2, route2EmpList);

	}

}
package src.main.java;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Route6 implements Route {
	List<Employee> route6EmpList;

	@Override
	public void createEmpListPerRoute(Employee e,
			Hashtable<RouteEnum, List<Employee>> routeMap) {
		if (null == routeMap.get(RouteEnum.Route6)) {
			route6EmpList = new ArrayList<Employee>();
		} else {
			route6EmpList = routeMap.get(RouteEnum.Route6);
		}
		route6EmpList.add(e);
		routeMap.put(RouteEnum.Route6, route6EmpList);

	}

}
